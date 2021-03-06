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

package org.semanticweb.owlapi.owllink.renderer;


import org.coode.owlapi.owlxml.renderer.OWLXMLObjectRenderer;
import org.coode.owlapi.owlxml.renderer.OWLXMLWriter;
import org.coode.xml.XMLWriter;
import org.coode.xml.XMLWriterNamespaceManager;
import org.semanticweb.owlapi.io.OWLRendererException;
import org.semanticweb.owlapi.io.OWLRendererIOException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.owllink.OWLlinkNamespaces;
import org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary;
import org.semanticweb.owlapi.owllink.PrefixManagerProvider;
import org.semanticweb.owlapi.util.CollectionFactory;
import org.semanticweb.owlapi.vocab.Namespaces;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.Map;

/**
 * @author Olaf Noppens
 */
public class OWLlinkXMLWriter {
    private XMLWriter writer;
    private PrefixManagerProvider prefixProvider;
    Map<IRI, OWLXMLObjectRenderer> rendererByIRI;
    OWLXMLObjectRenderer defaultRenderer;
    Writer baseWriter;

    public OWLlinkXMLWriter(Writer writer, PrefixManagerProvider prefixProvider) {
        XMLWriterNamespaceManager nsm = new XMLWriterNamespaceManager(OWLlinkNamespaces.OWLLink.toString() + "#");
        nsm.setPrefix("xsd", Namespaces.XSD.toString());
        nsm.setPrefix("owl", Namespaces.OWL.toString());
        String base = OWLlinkNamespaces.OWLLink.toString();
        //we need an own xml writer because in OWL attribute's NS are not allowed.
        this.writer = new MyXMLWriterImpl(writer, nsm, base) {
            @Override
            public void writeAttribute(String attr, String val) {
                if (attr.startsWith(Namespaces.OWL.toString())) {
                    String localName = attr.substring(Namespaces.OWL.toString().length(), attr.length());
                    super.writeAttribute(localName, val);
                } else
                    super.writeAttribute(attr, val);
            }

            @Override
            public void writeStartElement(String name) throws IOException {
                super.writeStartElement(name);    //To change body of overridden methods use File | Settings | File Templates.
            }
        };
        this.writer.setEncoding("UTF-8");
        OWLXMLWriter owlxmlWriter = new MyOWLXMLWriter(this.writer, null);
        this.defaultRenderer = new OWLXMLObjectRenderer(owlxmlWriter);
        this.baseWriter = writer;
        rendererByIRI = CollectionFactory.createMap();
        this.prefixProvider = prefixProvider;
    }

    public void startDocument(final boolean isRequest) throws OWLRendererException {
        try {
            if (isRequest)
                writer.startDocument(OWLlinkXMLVocabulary.REQUEST_MESSAGE.toString());
            else
                writer.startDocument(OWLlinkXMLVocabulary.RESPONSE_MESSAGE.toString());
        }
        catch (IOException e) {
            throw new OWLRendererIOException(e);
        }
    }


    public void endDocument() {
        try {
            writer.endDocument();
        }
        catch (IOException e) {
            throw new OWLRuntimeException(e);
        }
    }

    public final void writeStartElement(OWLlinkXMLVocabulary v) {
        this.writeStartElement(v.getURI());
    }

    public void writeStartElement(URI name) {
        try {
            writer.writeStartElement(name.toString());
        }
        catch (IOException e) {
            throw new OWLRuntimeException(e);
        }
    }


    public void writeEndElement() {
        try {
            writer.writeEndElement();
        }
        catch (IOException e) {
            throw new OWLRuntimeException(e);
        }
    }

    public void writeAttribute(String attribute, String value) {
        try {
            writer.writeAttribute(attribute, value);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeAttribute(URI attribute, String value) {
        try {
            writer.writeAttribute(attribute.toString(), value);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeNegativeAttribute(boolean isNegative) {
        try {
            writer.writeAttribute(OWLlinkXMLVocabulary.NEGATIVE_ATTRIBUTE.getURI().toString(), Boolean.toString(isNegative));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeDirectAttribute(boolean isNegative) {
        try {
            writer.writeAttribute(OWLlinkXMLVocabulary.DIRECT_ATTRIBUTE.getURI().toString(), Boolean.toString(isNegative));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeKBAttribute(IRI kb) {
        try {
            writer.writeAttribute(OWLlinkXMLVocabulary.KB_ATTRIBUTE.getURI().toString(), kb.toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFullIRIAttribute(IRI iri) {
        try {
            writer.writeAttribute(OWLlinkXMLVocabulary.IRI_ATTRIBUTE.getURI().toString(), iri.toString());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* public void writeIRIAttribute(IRI iri) {
  try {
      String attName = OWLXMLVocabulary.IRI_ATTRIBUTE.getURI().toString();
      String value = iri.toString();
      if (value.startsWith(writer.getXMLBase())) {
          writer.writeAttribute(attName, value.substring(writer.getXMLBase().length(), value.length()));
      } else {
          String val = getIRIString(iri.toURI());
          if (!val.equals(iri.toString())) {
              writer.writeAttribute(OWLXMLVocabulary.ABBREVIATED_IRI_ATTRIBUTE.getURI().toString(), val);
          } else {
              writer.writeAttribute(attName, val);
          }
      }
  }
  catch (IOException e) {
      throw new RuntimeException(e);
  }
}      */


    public void writeOWLObject(OWLObject object, IRI KB) {
        if (KB == null) {
            object.accept(defaultRenderer);
        } else {
            OWLXMLObjectRenderer renderer = rendererByIRI.get(KB);
            if (renderer == null) {
                //OWLXMLWriter writer = new OWLXMLWriter(baseWriter, null);
                OWLXMLWriter writer = new MyOWLXMLWriter(this.writer, null);
                if (prefixProvider.contains(KB)) {
                    Map<String, String> map = prefixProvider.getPrefixes(KB).getPrefixName2PrefixMap();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        writer.getIRIPrefixMap().put(entry.getValue(), entry.getKey());
                    }
                }
                renderer = new OWLXMLObjectRenderer(writer);
                rendererByIRI.put(KB, renderer);
            }
            object.accept(renderer);
        }
    }

    public void writeTextContent(String text) {
        try {
            writer.writeTextContent(text);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PrefixManagerProvider getPrefixManagerProvider() {
        return this.prefixProvider;
    }
}
