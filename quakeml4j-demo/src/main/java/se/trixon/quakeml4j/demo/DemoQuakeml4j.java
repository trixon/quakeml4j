/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package se.trixon.quakeml4j.demo;

import jakarta.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import se.trixon.quakeml4j.QuakeManager;
import se.trixon.quakeml4j.v1_2.Event;

/**
 *
 * @author Patrik Karlström <patrik@trixon.se>
 */
public class DemoQuakeml4j {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello QuakeML!");
        var f = new File("src/main/resources/001.xml");
        System.out.println(f);
        System.out.println(f.isFile());
        var qm = new QuakeManager();
        try {
            var quakeml = qm.read(f);

            if (quakeml.getEventParameters() != null) {
                for (var o : quakeml.getEventParameters().getCommentOrEventOrDescription()) {
                    if (o instanceof Event event) {
                        System.out.println(event);
//                        System.out.println(event.);
                    }
                    System.out.println(o);
                    System.out.println(o.getClass());
                }
            }

//            System.out.println(quakeml);
//            System.out.println(quakeml.getEventParameters().getAny().size());
//            System.out.println(quakeml.getEventParameters().getAny().getClass());
//            System.out.println(quakeml.getEventParameters());
//            System.out.println(quakeml.getEventParameters().getPublicID());
//        Files.list(Path.of()).forEach(p -> {
//        });
        } catch (JAXBException ex) {
            System.getLogger(DemoQuakeml4j.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
