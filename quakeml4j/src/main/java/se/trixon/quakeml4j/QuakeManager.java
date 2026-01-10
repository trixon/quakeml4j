/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package se.trixon.quakeml4j;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import se.trixon.quakeml4j.v1_2.Quakeml;

/**
 *
 * @author Patrik Karlström <patrik@trixon.se>
 */
public class QuakeManager {

    private JAXBContext mContext;
    private Unmarshaller mUnmarshaller;

    public QuakeManager() {
        try {
            mContext = JAXBContext.newInstance(Quakeml.class);
            mUnmarshaller = mContext.createUnmarshaller();
        } catch (JAXBException ex) {
            System.getLogger(QuakeManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    public Quakeml read(Object source) throws JAXBException {
        Object o = null;
        if (source instanceof File xml) {
            o = mUnmarshaller.unmarshal(xml);
        } else if (source instanceof InputSource xml) {
            o = mUnmarshaller.unmarshal(xml);
        } else if (source instanceof InputStream xml) {
            o = mUnmarshaller.unmarshal(xml);
        } else if (source instanceof Node xml) {
            o = mUnmarshaller.unmarshal(xml);
        } else if (source instanceof InputSource xml) {
            o = mUnmarshaller.unmarshal(xml);
        } else if (source instanceof Reader xml) {
            o = mUnmarshaller.unmarshal(xml);
        } else if (source instanceof Source xml) {
            o = mUnmarshaller.unmarshal(xml);
        } else if (source instanceof URL xml) {
            o = mUnmarshaller.unmarshal(xml);
        } else if (source instanceof XMLEventReader xml) {
            o = mUnmarshaller.unmarshal(xml);
        } else if (source instanceof XMLStreamReader xml) {
            o = mUnmarshaller.unmarshal(xml);
        } else {
        }

        return ((JAXBElement<Quakeml>) o).getValue();
    }
}
