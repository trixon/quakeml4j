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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;
import se.trixon.quakeml4j.QuakeParser;

/**
 *
 * @author Patrik Karlström <patrik@trixon.se>
 */
public class DemoQuakeml4j {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        System.out.println("DemoQuakeml4j");
        var f = new File("src/main/resources/001.xml");
//        f = new File("src/main/resources/qml-example-1.2-RC5.xml");

        var parser = new QuakeParser();
        for (var quake : parser.parse(f)) {
            System.out.println("");
            System.out.println(quake.getPublicId());
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
            System.out.println(quake.getMagnitude());
            System.out.println(quake.getMagnitudeType());
        }
    }
}
