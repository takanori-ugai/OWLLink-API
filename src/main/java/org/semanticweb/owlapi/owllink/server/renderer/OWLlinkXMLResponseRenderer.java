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

package org.semanticweb.owlapi.owllink.server.renderer;

import org.semanticweb.owlapi.io.OWLRendererException;
import org.semanticweb.owlapi.owllink.KBRequest;
import org.semanticweb.owlapi.owllink.PrefixManagerProvider;
import org.semanticweb.owlapi.owllink.Request;
import org.semanticweb.owlapi.owllink.Response;
import org.semanticweb.owlapi.owllink.renderer.OWLlinkXMLWriter;
import org.semanticweb.owlapi.owllink.server.response.ErrorResponse;
import org.semanticweb.owlapi.owllink.server.response.KBErrorResponse;
import org.semanticweb.owlapi.owllink.server.response.UnsatisfiableKBErrorResponse;
import org.semanticweb.owlapi.reasoner.Node;

import java.io.Writer;
import java.util.List;
import java.util.Map;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.BOOLEAN_RESPONSE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.BUILD_ATTRIBUTE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.CLASSES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.CLASS_HIERARCHY;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.CLASS_SUBCLASSESPAIR;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.CLASS_SYNSET;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.CLASS_SYNSETS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.DATAPROPERTY_HIERARCHY;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.DATAPROPERTY_SUBDATAPROPERTIESPAIR;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.DATAPROPERTY_SYNONYMS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.DATAPROPERTY_SYNSET;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.DATAPROPERTY_SYNSETS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.DATATYPE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.DESCRIPTION;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.ERROR;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.ERROR_ATTRIBUTE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.FULL_IRI_ATTRIBUTE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.IDENTIFIER_ATTRIBUTE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.INDIVIDUAL_SYNONYMS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.INDIVIDUAL_SYNSET;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.IRI_ATTRIBUTE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.KB_ATTRIBUTE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.KB_ERROR;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.KB_RESPONSE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.KEY_ATTRIBUTE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.LIST;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.LITERAL;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.MAJOR_ATTRIBUTE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.MINOR_ATTRIBUTE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.NAME_Attribute;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.OBJECTPROPERTY_HIERARCHY;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.OBJECTPROPERTY_SUBOBJECTPROPERTIESPAIR;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.OBJECTPROPERTY_SYNSET;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.OBJECTPROPERTY_SYNSETS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.OK;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.ONEOF;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.PREFIX;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.PREFIXES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.PROPERTY;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.PROTOCOLVERSION;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.PublicKB;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.REASONERVERSION;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.RESULT_ATTRIBUTE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SETTING;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SETTINGS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SET_OF_ANNOTATIONPROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SET_OF_CLASSES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SET_OF_CLASS_SYNSETS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SET_OF_DATAPROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SET_OF_DATAPROPERTY_SYNSETS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SET_OF_DATATYPES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SET_OF_INDIVIDUALS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SET_OF_INDIVIDUALS_SYNSETS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SET_OF_LITERALS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SET_OF_OBJECTPROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SET_OF_OBJECTPROPERTY_SYNSETS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SUBCLASS_SYNSETS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SUBDATAPROPERTY_SYNSETS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SUBOBJECTPROPERTY_SYNSETS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SUPPORTEDEXTENSION;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.String_RESPONSE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.UNSATISFIABLEKBERROR;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.WARNING_ATTRIBUTE;
import org.semanticweb.owlapi.owllink.builtin.response.BooleanResponse;
import org.semanticweb.owlapi.owllink.builtin.response.ClassHierarchy;
import org.semanticweb.owlapi.owllink.builtin.response.ClassSynsets;
import org.semanticweb.owlapi.owllink.builtin.response.Classes;
import org.semanticweb.owlapi.owllink.builtin.response.Configuration;
import org.semanticweb.owlapi.owllink.builtin.response.Confirmation;
import org.semanticweb.owlapi.owllink.builtin.response.DataPropertyHierarchy;
import org.semanticweb.owlapi.owllink.builtin.response.DataPropertySynonyms;
import org.semanticweb.owlapi.owllink.builtin.response.DataPropertySynsets;
import org.semanticweb.owlapi.owllink.builtin.response.Description;
import org.semanticweb.owlapi.owllink.builtin.response.HierarchyPair;
import org.semanticweb.owlapi.owllink.builtin.response.IndividualSynonyms;
import org.semanticweb.owlapi.owllink.builtin.response.IndividualSynset;
import org.semanticweb.owlapi.owllink.builtin.response.KB;
import org.semanticweb.owlapi.owllink.builtin.response.KBResponse;
import org.semanticweb.owlapi.owllink.builtin.response.OK;
import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkDataRange;
import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkDatatype;
import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkList;
import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkLiteral;
import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkOneOf;
import org.semanticweb.owlapi.owllink.builtin.response.ObjectPropertyHierarchy;
import org.semanticweb.owlapi.owllink.builtin.response.ObjectPropertySynsets;
import org.semanticweb.owlapi.owllink.builtin.response.Prefixes;
import org.semanticweb.owlapi.owllink.builtin.response.Property;
import org.semanticweb.owlapi.owllink.builtin.response.PublicKB;
import org.semanticweb.owlapi.owllink.builtin.response.ResponseVisitor;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfAnnotationProperties;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfClassSynsets;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfClasses;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfDataProperties;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfDataPropertySynsets;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfDatatypes;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividualSynsets;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfIndividuals;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfLiterals;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfObjectProperties;
import org.semanticweb.owlapi.owllink.builtin.response.SetOfObjectPropertySynsets;
import org.semanticweb.owlapi.owllink.builtin.response.Setting;
import org.semanticweb.owlapi.owllink.builtin.response.Settings;
import org.semanticweb.owlapi.owllink.builtin.response.StringResponse;
import org.semanticweb.owlapi.owllink.builtin.response.SubEntitySynsets;

/**
 * @author Olaf Noppens
 */
public class OWLlinkXMLResponseRenderer {

    IRI defaultKB;
    ResponseRenderer renderer;

    public OWLlinkXMLResponseRenderer() {
        this.renderer = new ResponseRenderer();
    }

    @Deprecated
    public void render(Writer writer, PrefixManagerProvider prov, IRI defaultKB, Response... responses) throws OWLRendererException {
        this.defaultKB = defaultKB;
        OWLlinkXMLWriter w = new OWLlinkXMLWriter(writer, prov);
        w.startDocument(false);
        renderer.setWriter(w);
        for (Response response : responses) {
            //getRequestRenderer(request).render(request, w);
            //render(response, w);
            if (response instanceof KBResponse) {

            }
            response.accept(renderer);
        }
        w.endDocument();
    }


    public void render(Writer writer, PrefixManagerProvider prov, List<Request> requests, List<Response> responses) throws OWLRendererException {
        OWLlinkXMLWriter w = new OWLlinkXMLWriter(writer, prov);
        w.startDocument(false);
        renderer.setWriter(w);
        int count = responses.size();
        for (int i = 0; i < count; i++) {
            Response response = responses.get(i);
            if (response instanceof KBResponse) {
                this.defaultKB = ((KBRequest) requests.get(i)).getKB();
            }
            response.accept(renderer);
        }
        w.endDocument();
    }

    class ResponseRenderer implements ResponseVisitor<Void> {
        OWLlinkXMLWriter writer;

        public void setWriter(OWLlinkXMLWriter writer) {
            this.writer = writer;
        }

        public final void renderWarning(final Confirmation response) {
            if (response.hasWarning())
                writer.writeAttribute(WARNING_ATTRIBUTE.getURI(), response.getWarning());
        }


        public void renderClassSynset(Node<OWLClass> synset) {
            writer.writeStartElement(CLASS_SYNSET);
            for (OWLClass clazz : synset)
                writer.writeOWLObject(clazz, defaultKB);
            writer.writeEndElement();
        }

        public void renderClassSubClassesPair(HierarchyPair<OWLClass> pair) {
            writer.writeStartElement(CLASS_SUBCLASSESPAIR);
            renderClassSynset(pair.getSuper());
            renderSetOfSubClassSynsets(pair.getSubs());
            writer.writeEndElement();
        }

        public void renderSetOfSubClassSynsets(SubEntitySynsets<OWLClass> setofsubsynsets) {
            writer.writeStartElement(SUBCLASS_SYNSETS);
            for (Node<OWLClass> synset : setofsubsynsets)
                renderClassSynset(synset);
            writer.writeEndElement();
        }

        public void renderObjectPropertySynset(Node<OWLObjectProperty> synset) {
            writer.writeStartElement(OBJECTPROPERTY_SYNSET);
            for (OWLObjectProperty prop : synset)
                writer.writeOWLObject(prop, defaultKB);
            writer.writeEndElement();
        }


        public void renderObjectPropertySubPair(HierarchyPair<OWLObjectProperty> pair) {
            writer.writeStartElement(OBJECTPROPERTY_SUBOBJECTPROPERTIESPAIR);
            renderObjectPropertySynset(pair.getSuper());
            renderSetOfSubObjectPropertySynsets(pair.getSubs());
            writer.writeEndElement();
        }

        public void renderSetOfSubObjectPropertySynsets(SubEntitySynsets<OWLObjectProperty> setofsubsynsets) {
            writer.writeStartElement(SUBOBJECTPROPERTY_SYNSETS);
            for (Node<OWLObjectProperty> synset : setofsubsynsets)
                renderObjectPropertySynset(synset);
            writer.writeEndElement();
        }

        public void renderDataPropertySynset(Node<OWLDataProperty> synset) {
            writer.writeStartElement(DATAPROPERTY_SYNSET);
            for (OWLDataProperty clazz : synset)
                writer.writeOWLObject(clazz, defaultKB);
            writer.writeEndElement();
        }

        public void renderDataPropertySubPair(HierarchyPair<OWLDataProperty> pair) {
            writer.writeStartElement(DATAPROPERTY_SUBDATAPROPERTIESPAIR);
            renderDataPropertySynset(pair.getSuper());
            renderSetOfSubDataPropertySynsets(pair.getSubs());
            writer.writeEndElement();
        }

        public void renderSetOfSubDataPropertySynsets(SubEntitySynsets<OWLDataProperty> setofsubsynsets) {
            writer.writeStartElement(SUBDATAPROPERTY_SYNSETS);
            for (Node<OWLDataProperty> synset : setofsubsynsets)
                renderDataPropertySynset(synset);
            writer.writeEndElement();
        }

        public void renderIndividualSynset(IndividualSynset synset) {
            writer.writeStartElement(INDIVIDUAL_SYNSET);
            for (OWLIndividual indi : synset)
                writer.writeOWLObject(indi, defaultKB);
            writer.writeEndElement();
        }

        public Void visit(Response response) {
            if (response instanceof KBErrorResponse)
                visit((KBErrorResponse) response);
            else if (response instanceof UnsatisfiableKBErrorResponse)
                visit((UnsatisfiableKBErrorResponse) response);
            else if (response instanceof ErrorResponse)
                visit((ErrorResponse) response);
            return null;
        }

        public Void visit(KBResponse response) {
            return null;
        }

        public void visit(ErrorResponse response) {
            writer.writeStartElement(ERROR);
            writer.writeAttribute(ERROR_ATTRIBUTE.getURI(), response.getErrorString());
            writer.writeEndElement();
        }

        public void visit(KBErrorResponse response) {
            writer.writeStartElement(KB_ERROR);
            writer.writeAttribute(ERROR_ATTRIBUTE.getURI(), response.getErrorString());
            writer.writeEndElement();
        }

        public void visit(UnsatisfiableKBErrorResponse response) {
            writer.writeStartElement(UNSATISFIABLEKBERROR);
            writer.writeAttribute(ERROR_ATTRIBUTE.getURI(), response.getErrorString());
            writer.writeEndElement();
        }


        public Void visit(BooleanResponse response) {
            writer.writeStartElement(BOOLEAN_RESPONSE);
            writer.writeAttribute(RESULT_ATTRIBUTE.getURI(), response.getResult().toString());
            writer.writeEndElement();
            return null;
        }

        public Void visit(Classes response) {
            writer.writeStartElement(CLASSES);
            for (OWLClass clazz : response) {
                writer.writeOWLObject(clazz, defaultKB);
            }
            writer.writeEndElement();
            return null;
        }

        public Void visit(ClassHierarchy response) {
            writer.writeStartElement(CLASS_HIERARCHY);
            renderWarning(response);
            renderClassSynset(response.getUnsatisfiables());
            for (HierarchyPair<OWLClass> pair : response.getPairs()) {
                renderClassSubClassesPair(pair);
            }
            writer.writeEndElement();
            return null;
        }

        public Void visit(ClassSynsets response) {
            writer.writeStartElement(CLASS_SYNSETS);
            renderWarning(response);
            for (Node<OWLClass> synset : response) {
                renderClassSynset(synset);
            }
            writer.writeEndElement();
            return null;
        }

        public Void visit(DataPropertyHierarchy response) {
            writer.writeStartElement(DATAPROPERTY_HIERARCHY);
            renderWarning(response);
            renderDataPropertySynset(response.getUnsatisfiables());
            for (HierarchyPair<OWLDataProperty> pair : response.getPairs()) {
                renderDataPropertySubPair(pair);
            }
            writer.writeEndElement();
            return null;
        }

        public Void visit(DataPropertySynsets response) {
            writer.writeStartElement(DATAPROPERTY_SYNSETS);
            renderWarning(response);
            for (Node<OWLDataProperty> synset : response)
                renderDataPropertySynset(synset);
            writer.writeEndElement();
            return null;
        }

        public Void visit(DataPropertySynonyms response) {
            writer.writeStartElement(DATAPROPERTY_SYNONYMS);
            renderWarning(response);
            for (OWLDataProperty prop : response) {
                writer.writeOWLObject(prop, defaultKB);
            }
            writer.writeEndElement();
            return null;
        }

        public Void visit(Description response) {
            writer.writeStartElement(DESCRIPTION);
            renderWarning(response);
            writer.writeAttribute(NAME_Attribute.getURI(), response.getName());
            writer.writeStartElement(PROTOCOLVERSION);
            writer.writeAttribute(MAJOR_ATTRIBUTE.getURI(), Integer.toString(response.getProtocolVersion().getMajor()));
            writer.writeAttribute(MINOR_ATTRIBUTE.getURI(), Integer.toString(response.getProtocolVersion().getMinor()));
            writer.writeEndElement();

            writer.writeStartElement(REASONERVERSION);
            writer.writeAttribute(MAJOR_ATTRIBUTE.getURI(), Integer.toString(response.getReasonerVersion().getMajor()));
            writer.writeAttribute(MINOR_ATTRIBUTE.getURI(), Integer.toString(response.getReasonerVersion().getMinor()));
            writer.writeAttribute(BUILD_ATTRIBUTE.getURI(), Integer.toString(response.getReasonerVersion().getBuild()));
            writer.writeEndElement();

            for (Configuration configuration : response.getDefaults()) {
                if (configuration.isSetting()) {
                    writer.writeStartElement(SETTING);
                    writer.writeAttribute(KEY_ATTRIBUTE.getURI(), configuration.getKey());
                    Setting setting = configuration.asSetting();
                    visit(setting.getType());
                    for (OWLlinkLiteral literal : setting.getValues()) {
                        visit(literal);
                    }
                } else {
                    Property propety = configuration.asProperty();
                    writer.writeStartElement(PROPERTY);
                    writer.writeAttribute(KEY_ATTRIBUTE.getURI(), configuration.getKey());
                    visit(propety.getType());
                    for (OWLlinkLiteral literal : propety.getValues()) {
                        visit(literal);
                    }
                    writer.writeEndElement();
                }
            }
            for (PublicKB kb : response.getPublicKBs()) {
                writer.writeStartElement(PublicKB);
                writer.writeAttribute(NAME_Attribute.getURI(), kb.getName());
                writer.writeAttribute(KB_ATTRIBUTE.getURI(), kb.getKB().toString());
                writer.writeEndElement();
            }
            for (IRI iri : response.getSupportedExtensions()) {
                writer.writeStartElement(SUPPORTEDEXTENSION);
                writer.writeAttribute(IDENTIFIER_ATTRIBUTE.getURI(), iri.toString());
                writer.writeEndElement();
            }

            writer.writeEndElement();
            return null;
        }

        public void visit(OWLlinkDataRange dataRange) {
            if (dataRange instanceof OWLlinkOneOf)
                visit((OWLlinkOneOf) dataRange);
            else if (dataRange instanceof OWLlinkList)
                visit((OWLlinkList) dataRange);
            else if (dataRange instanceof OWLlinkDatatype) {
                visit((OWLlinkDatatype) dataRange);
            }
        }

        public void visit(OWLlinkOneOf oneOf) {
            writer.writeStartElement(ONEOF);
            writer.writeAttribute(IRI_ATTRIBUTE.toString(), oneOf.getType().toString());
            for (OWLlinkLiteral literal : oneOf.getLiterals()) {
                visit(literal);
            }
            writer.writeEndElement();
        }

        public void visit(OWLlinkList list) {
            writer.writeStartElement(LIST);
            writer.writeAttribute(IRI_ATTRIBUTE.toString(), list.getType().toString());
            writer.writeEndElement();
        }

        public void visit(OWLlinkDatatype datatype) {
            writer.writeStartElement(DATATYPE);
            writer.writeFullIRIAttribute(datatype.getIRI());
            writer.writeEndElement();
        }

        public void visit(OWLlinkLiteral literal) {
            writer.writeStartElement(LITERAL);
            writer.writeTextContent(literal.getValue());
            writer.writeEndElement();
        }

        public Void visit(IndividualSynonyms response) {
            writer.writeStartElement(INDIVIDUAL_SYNONYMS);
            renderWarning(response);
            for (OWLIndividual individual : response)
                writer.writeOWLObject(individual, defaultKB);
            writer.writeEndElement();
            return null;
        }

        public Void visit(KB response) {
            writer.writeStartElement(KB_RESPONSE);
            renderWarning(response);
            writer.writeKBAttribute(response.getKB());
            writer.writeEndElement();
            return null;
        }

        public Void visit(ObjectPropertyHierarchy response) {
            writer.writeStartElement(OBJECTPROPERTY_HIERARCHY);
            renderWarning(response);
            renderObjectPropertySynset(response.getUnsatisfiables());
            for (HierarchyPair<OWLObjectProperty> pair : response.getPairs()) {
                renderObjectPropertySubPair(pair);
            }
            writer.writeEndElement();
            return null;
        }

        public Void visit(ObjectPropertySynsets response) {
            writer.writeStartElement(OBJECTPROPERTY_SYNSETS);
            renderWarning(response);
            for (Node<OWLObjectProperty> synset : response)
                renderObjectPropertySynset(synset);
            writer.writeEndElement();
            return null;
        }

        public Void visit(OK response) {
            writer.writeStartElement(OK);
            renderWarning(response);
            writer.writeEndElement();
            return null;
        }

        public Void visit(Prefixes response) {
            writer.writeStartElement(PREFIXES);
            renderWarning(response);
            Map<String, String> map = response.getPrefixes();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                writer.writeStartElement(PREFIX);
                writer.writeAttribute(NAME_Attribute.getURI(), entry.getKey().endsWith(":") ? entry.getKey().substring(0, entry.getKey().length() - 1) : entry.getKey());
                writer.writeAttribute(FULL_IRI_ATTRIBUTE.getURI(), entry.getValue().toString());
                writer.writeEndElement();
            }
            writer.writeEndElement();
            return null;
        }

        public Void visit(SetOfAnnotationProperties response) {
            writer.writeStartElement(SET_OF_ANNOTATIONPROPERTIES);
            renderWarning(response);
            for (OWLAnnotationProperty annotation : response)
                writer.writeOWLObject(annotation, defaultKB);
            writer.writeEndElement();
            return null;
        }

        public Void visit(SetOfClasses response) {
            writer.writeStartElement(SET_OF_CLASSES);
            renderWarning(response);
            for (OWLClass clazz : response)
                writer.writeOWLObject(clazz, defaultKB);
            writer.writeEndElement();
            return null;
        }

        public Void visit(SetOfClassSynsets response) {
            writer.writeStartElement(SET_OF_CLASS_SYNSETS);
            renderWarning(response);
            for (Node<OWLClass> synset : response) {
                renderClassSynset(synset);
            }
            writer.writeEndElement();
            return null;
        }

        public Void visit(SetOfDataProperties response) {
            writer.writeStartElement(SET_OF_DATAPROPERTIES);
            renderWarning(response);
            for (OWLDataProperty prop : response)
                writer.writeOWLObject(prop, defaultKB);
            writer.writeEndElement();
            return null;
        }

        public Void visit(SetOfDataPropertySynsets response) {
            writer.writeStartElement(SET_OF_DATAPROPERTY_SYNSETS);
            renderWarning(response);
            for (Node<OWLDataProperty> prop : response)
                renderDataPropertySynset(prop);
            writer.writeEndElement();
            return null;
        }

        public Void visit(SetOfDatatypes response) {
            writer.writeStartElement(SET_OF_DATATYPES);
            renderWarning(response);
            for (OWLDatatype type : response)
                writer.writeOWLObject(type, defaultKB);
            writer.writeEndElement();
            return null;
        }

        public Void visit(SetOfIndividuals response) {
            writer.writeStartElement(SET_OF_INDIVIDUALS);
            renderWarning(response);
            for (OWLIndividual indi : response)
                writer.writeOWLObject(indi, defaultKB);
            writer.writeEndElement();
            return null;
        }

        public Void visit(SetOfIndividualSynsets response) {
            writer.writeStartElement(SET_OF_INDIVIDUALS_SYNSETS);
            renderWarning(response);
            for (IndividualSynset synset : response.getSynsets())
                renderIndividualSynset(synset);
            writer.writeEndElement();
            return null;
        }

        public Void visit(SetOfLiterals response) {
            writer.writeStartElement(SET_OF_LITERALS);
            renderWarning(response);
            for (OWLLiteral literal : response)
                writer.writeOWLObject(literal, defaultKB);
            writer.writeEndElement();
            return null;
        }

        public Void visit(SetOfObjectProperties response) {
            writer.writeStartElement(SET_OF_OBJECTPROPERTIES);
            renderWarning(response);
            for (OWLObjectProperty prop : response)
                writer.writeOWLObject(prop, defaultKB);
            writer.writeEndElement();
            return null;
        }

        public Void visit(SetOfObjectPropertySynsets response) {
            writer.writeStartElement(SET_OF_OBJECTPROPERTY_SYNSETS);
            renderWarning(response);
            for (Node<OWLObjectProperty> prop : response)
                renderObjectPropertySynset(prop);
            writer.writeEndElement();
            return null;
        }

        public Void visit(Settings response) {
            writer.writeStartElement(SETTINGS);
            renderWarning(response);
            for (Setting setting : response) {
                writer.writeStartElement(SETTING);
                writer.writeAttribute(KEY_ATTRIBUTE.getURI(), setting.getKey());
                visit(setting.getType());
                for (OWLlinkLiteral literal : setting.getValues()) {
                    visit(literal);
                }
            }
            writer.writeEndElement();
            return null;
        }

        public Void visit(StringResponse response) {
            writer.writeStartElement(String_RESPONSE);
            renderWarning(response);
            writer.writeAttribute(RESULT_ATTRIBUTE.getURI(), response.getResult());
            writer.writeEndElement();
            return null;
        }
    }

}
