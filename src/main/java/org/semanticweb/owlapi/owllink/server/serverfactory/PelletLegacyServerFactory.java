/*
 * Copyright (C) 2010, Ulm University
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.semanticweb.owlapi.owllink.server.serverfactory;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.model.OWLOntologyChangeListener;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owlapi.owllink.server.AbstractOWLlinkReasonerConfiguration;
import org.semanticweb.owlapi.owllink.server.OWLlinkHTTPXMLServer;
import org.semanticweb.owlapi.owllink.server.legacy.LegacyReasonerFactory;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.reasonerfactory.pellet.PelletReasonerFactory;

/**
 * Created by IntelliJ IDEA.
 * Author: Olaf Noppens
 * Date: 25.11.2009
 */
public class PelletLegacyServerFactory implements OWLlinkServerFactory {

    static void usage() {
        System.out.println("Pellet using OWLAPI v2 as OWLlink Server");
        System.out.println("OWLlink server that is backed by Pellet reasoner");
        System.out.println("");
        System.out.println("Usage: java PelletServerFactory [-port portNum]");
        System.out.println("   -port portNum           The port number user by the server (default");
        System.out.println("                           port number used is 8080)");
        System.out.println("   -help                   Print this information");
        System.out.println("Make sure that the pellet binaries are in your classpath or set it via \"-cp\"");
    }


    public static void main(String args[]) {
        int port = 8080;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equalsIgnoreCase("-help")) {
                usage();
                System.exit(0);
            } else if (arg.equalsIgnoreCase("-port")) {
                try {
                    port = Integer.parseInt(args[++i]);
                } catch (NumberFormatException e1) {
                    System.err.println("Invalid port number: " + args[i]);
                    System.exit(1);
                }
            } else {
                System.err.println("Unrecognized option: " + arg);
                usage();
                System.exit(1);
            }
        }

        PelletLegacyServerFactory factory = new PelletLegacyServerFactory();
        OWLlinkHTTPXMLServer server = factory.createServer(port);
        server.run();
    }

    public OWLlinkHTTPXMLServer createServer(int port) {
        PelletReasonerFactory factory = new PelletReasonerFactory() {
            @Override
            public OWLReasoner createReasoner(OWLOntologyManager manager) {
                OWLReasoner reasoner = super.createReasoner(manager);    //To change body of overridden methods use File | Settings | File Templates.
                manager.addOntologyChangeListener((OWLOntologyChangeListener) reasoner);
                return reasoner;
            }
        };
        LegacyReasonerFactory legacyPellet = new LegacyReasonerFactory(factory);
        AbstractOWLlinkReasonerConfiguration config = new AbstractOWLlinkReasonerConfiguration((OWLReasonerConfiguration) null);
        config.setSupportedDatatypes(OWL2Datatype.XSD_LONG.getIRI(),
                OWL2Datatype.XSD_INT.getIRI(),
                OWL2Datatype.XSD_NON_POSITIVE_INTEGER.getIRI(),
                OWL2Datatype.XSD_NON_NEGATIVE_INTEGER.getIRI(),
                OWL2Datatype.XSD_SHORT.getIRI(),
                OWL2Datatype.OWL_REAL.getIRI());

        OWLlinkHTTPXMLServer server = new OWLlinkHTTPXMLServer(legacyPellet, config, port);
        return server;

    }
}
