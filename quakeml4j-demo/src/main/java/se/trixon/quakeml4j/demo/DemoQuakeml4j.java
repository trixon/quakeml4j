/*
 * Copyright 2026 Patrik Karlström <patrik@trixon.se>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.trixon.quakeml4j.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;
import se.trixon.quakeml4j.Quake;
import se.trixon.quakeml4j.QuakeParser;

/**
 *
 * @author Patrik Karlström <patrik@trixon.se>
 */
public class DemoQuakeml4j {

    private final QuakeParser parser = new QuakeParser();

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        System.out.println("DemoQuakeml4j");
        var demo = new DemoQuakeml4j();
//        demo.parseFile();
        demo.parseDir();;
    }

    public DemoQuakeml4j() {
    }

    private void parseDir() {
        var t0 = LocalTime.now();
        var quakes = parser.parseRecursive(new File("/home/pata/snsn/emsc/2025/"));
//        var quakes = parser.parseRecursive(new File("/home/pata/.cache/mapton/earthquakes/"));
        var t1 = LocalTime.now();
        var t = ChronoUnit.SECONDS.between(t0, t1);
        System.out.format("Time: %d sec, Quakes: %d\n", t, quakes.size());

    }

    private void parseDirOLD(File dir) {
        var earthquakes = new ConcurrentLinkedQueue<Quake>();
        var existingIds = new HashSet<String>();
        var semaphore = new Semaphore(50);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            try (var paths = Files.walk(Paths.get(dir.toURI()))) {
                paths.filter(Files::isRegularFile)
                        .map(path -> path.toFile())
                        .filter(file -> file.getName().endsWith(".xml") && file.length() > 0)
                        .forEach(file -> {
                            executor.submit(() -> {
                                try {
                                    semaphore.acquire();
                                    var earthquakeFileItems = parser.parse(file).stream()
                                            .filter(xmlQuake -> !existingIds.contains(xmlQuake.getPublicId()))
                                            .toList();
                                    earthquakes.addAll(earthquakeFileItems);
                                } catch (IOException | XPathExpressionException | ParserConfigurationException | SAXException ex) {
                                    System.getLogger(DemoQuakeml4j.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                                } finally {
                                    semaphore.release();
                                }
                                return null;
                            });
                        });
            } catch (IOException ex) {
                System.getLogger(DemoQuakeml4j.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }

        System.out.format("Quakes: %d\n", earthquakes.size());
    }

    private void parseFile() {
        try {
            var f = new File("/home/pata/usgs.xml");
//            var f = new File("src/main/resources/001.xml");
//        f = new File("src/main/resources/qml-example-1.2-RC5.xml");

            for (var quake : parser.parse(f)) {
                System.out.println("");
                System.out.println(quake.getPublicId());
                System.out.println(quake.getPlace());
                System.out.println(quake.getType());
                System.out.println(quake.getTypeCertainty());
                System.out.println(quake.getCreationInfo());
                System.out.println(quake.getCreationInfo().getAgencyId());
                System.out.println(quake.getCreationInfo().getAuthor());
                System.out.println(quake.getCreationInfo().getCreationTime());
                System.out.println(quake.getOrigin().getTime());
                System.out.println(quake.getOrigin().getLatitude());
                System.out.println(quake.getOrigin().getLongitude());
                System.out.println(quake.getOrigin().getDepth());
                System.out.println(quake.getOrigin().getDepthType());
                System.out.println(quake.getOrigin().getType());
                System.out.println(quake.getMagnitude().getValue());
                System.out.println(quake.getMagnitude().getType());
            }
        } catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException ex) {
            System.getLogger(DemoQuakeml4j.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
