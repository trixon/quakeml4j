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
package se.trixon.quakeml4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Patrik Karlström <patrik@trixon.se>
 */
public class QuakeParser {

    private XPathExpression mCreationInfoAgencyIdExpression;
    private XPathExpression mCreationInfoAuthorExpression;
    private XPathExpression mCreationInfoCreationTimeExpression;
    private DocumentBuilderFactory mDocumentBuilderFactory;
    private XPathExpression mEventListExpression;
    private XPathExpression mOriginDepthExpression;
    private XPathExpression mOriginDepthTypeExpression;
    private XPathExpression mOriginLatExpression;
    private XPathExpression mOriginLonExpression;
    private XPathExpression mOriginTimeExpression;
    private XPathExpression mOriginTypeExpression;
    private XPathExpression mRootMagnitudeExpression;
    private XPathExpression mRootMagnitudeTypeExpression;
    private XPathExpression mRootPublicIdExpression;
    private XPathExpression mRootTypeCertaintyExpression;
    private XPathExpression mRootTypeExpression;
    private XPath mxPath;

    public QuakeParser() {
        mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        mDocumentBuilderFactory.setNamespaceAware(true);
        mxPath = XPathFactory.newInstance().newXPath();
        mxPath.setNamespaceContext(new QuakeNamespaceContext());

        try {
            mEventListExpression = mxPath.compile("//bed:event");
            mRootPublicIdExpression = mxPath.compile("@publicID");
            mRootTypeExpression = mxPath.compile("bed:type");
            mRootTypeCertaintyExpression = mxPath.compile("bed:typeCertainty");
            mRootMagnitudeExpression = mxPath.compile("bed:magnitude/bed:mag/bed:value");
            mRootMagnitudeTypeExpression = mxPath.compile("bed:magnitude/bed:type");
            //
            mCreationInfoAgencyIdExpression = mxPath.compile("bed:creationInfo/bed:agencyID");
            mCreationInfoAuthorExpression = mxPath.compile("bed:creationInfo/bed:author");
            mCreationInfoCreationTimeExpression = mxPath.compile("bed:creationInfo/bed:creationTime");
            //
            mOriginTimeExpression = mxPath.compile("bed:origin/bed:time/bed:value");
            mOriginLatExpression = mxPath.compile("bed:origin/bed:latitude/bed:value");
            mOriginLonExpression = mxPath.compile("bed:origin/bed:longitude/bed:value");
            mOriginDepthExpression = mxPath.compile("bed:origin/bed:depth/bed:value");
            mOriginDepthTypeExpression = mxPath.compile("bed:origin/bed:depthType");
            mOriginTypeExpression = mxPath.compile("bed:origin/bed:type");
        } catch (XPathExpressionException ex) {
            System.getLogger(QuakeParser.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public List<Quake> parse(File file) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
        var doc = mDocumentBuilderFactory.newDocumentBuilder().parse(file);

        var quakes = new ArrayList<Quake>();
        var eventNodes = (NodeList) mEventListExpression.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < eventNodes.getLength(); i++) {
            var eventNode = eventNodes.item(i);
            var q = new Quake();
            q.setPublicId(mRootPublicIdExpression.evaluate(eventNode));
            q.setType(Quake.Type.fromValue(mRootTypeExpression.evaluate(eventNode)));
            q.setTypeCertainty(Quake.TypeCertainty.fromValue(mRootTypeCertaintyExpression.evaluate(eventNode)));
            q.setMagnitude(getAsDouble(eventNode, mRootMagnitudeExpression));
            q.setMagnitudeType(mRootMagnitudeTypeExpression.evaluate(eventNode));
//
            q.getCreationInfo().setAgencyId(mCreationInfoAgencyIdExpression.evaluate(eventNode));
            q.getCreationInfo().setAuthor(mCreationInfoAuthorExpression.evaluate(eventNode));
            q.getCreationInfo().setCreationTime(OffsetDateTime.parse(mCreationInfoCreationTimeExpression.evaluate(eventNode)));
//
            q.getOrigin().setTime(OffsetDateTime.parse(mOriginTimeExpression.evaluate(eventNode)));
            q.getOrigin().setLatitude(getAsDouble(eventNode, mOriginLatExpression));
            q.getOrigin().setLongitude(getAsDouble(eventNode, mOriginLonExpression));
            q.getOrigin().setDepth(getAsDouble(eventNode, mOriginDepthExpression));
            q.getOrigin().setDepthType(Origin.DepthType.fromValue(mOriginDepthTypeExpression.evaluate(eventNode)));
            q.getOrigin().setType(Origin.Type.fromValue(mOriginTypeExpression.evaluate(eventNode)));
//
            quakes.add(q);
        }

        return quakes;
    }

    public ConcurrentLinkedQueue<Quake> parseRecursive(File dir) {
        var quakes = new ConcurrentLinkedQueue<Quake>();
        var existingIds = ConcurrentHashMap.newKeySet();
        var semaphore = new Semaphore(Runtime.getRuntime().availableProcessors() * 2);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            try (var paths = Files.walk(Paths.get(dir.toURI()))) {
                paths.filter(Files::isRegularFile)
                        .map(path -> path.toFile())
                        .filter(file -> file.getName().endsWith(".xml") && file.length() > 0)
                        .forEach(file -> {
                            executor.submit(() -> {
                                try {
                                    semaphore.acquire();
                                    var earthquakeFileItems = parse(file).stream()
                                            .filter(quake -> {
                                                if (existingIds.contains(quake.getPublicId())) {
                                                    return false;
                                                } else {
                                                    existingIds.add(quake.getPublicId());
                                                    return true;
                                                }
                                            })
                                            .toList();
                                    quakes.addAll(earthquakeFileItems);
                                } catch (IOException | XPathExpressionException | ParserConfigurationException | SAXException ex) {
                                    System.getLogger(QuakeParser.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                                } finally {
                                    semaphore.release();
                                }
                                return null;
                            });
                        });
            } catch (IOException ex) {
                System.getLogger(QuakeParser.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }

        return quakes;
    }

    private Double getAsDouble(Node node, XPathExpression expression) throws XPathExpressionException {
        var s = expression.evaluate(node);
        if (!s.isEmpty()) {
            try {
                return Double.valueOf(s);
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }

    }

    public class QuakeNamespaceContext implements NamespaceContext {

        @Override
        public String getNamespaceURI(String prefix) {
            if ("q".equals(prefix)) {
                return "http://quakeml.org/xmlns/quakeml/1.2";
            }
            if ("bed".equals(prefix)) {
                return "http://quakeml.org/xmlns/bed/1.2";
            }
            return XMLConstants.NULL_NS_URI;
        }

        @Override
        public String getPrefix(String uri) {
            return null;
        }

        @Override
        public Iterator getPrefixes(String uri) {
            return null;
        }
    }

}
