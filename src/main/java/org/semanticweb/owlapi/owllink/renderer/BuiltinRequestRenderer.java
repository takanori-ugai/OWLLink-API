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

import org.semanticweb.owlapi.io.OWLRendererException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.owllink.Request;
import org.semanticweb.owlapi.owllink.builtin.response.OWLlinkLiteral;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.util.Map;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.CLASSIFY;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.CONSIDER_IMPORTS_ATTRIBUTE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.FULLIRI;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_ALL_ANNOTATION_PROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_ALL_CLASSES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_ALL_DATAPROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_ALL_DATATYPES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_ALL_INDIVIDUALS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_ALL_OBJECTPROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_DATAPROPERTIES_BETWEEN;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_DATAPROPERTIES_OF_LITERAL;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_DATAPROPERTIES_OF_SOURCE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_DATAPROPERTY_SOURCES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_DATAPROPERTY_TARGETS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_DESCRIPTION;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_DIFFERENT_INDIVIDUALS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_DISJOINT_CLASSES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_DISJOINT_DATAPROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_DISJOINT_OBJECTPROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_EQUIVALENT_CLASSES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_EQUIVALENT_DATAPROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_EQUIVALENT_OBJECTPROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_FLATTENED_DATAPROPERTY_SOURCES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_FLATTENED_DIFFERENT_INDIVIDUALS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_FLATTENED_INSTANCES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_FLATTENED_OBJECTPROPERTY_SOURCES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_FLATTENED_OBJECTPROPERTY_TARGETS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_FLATTENED_TYPES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_INSTANCES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_KB_LANGUAGE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_OBJECTPROPERTIES_BETWEEN;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_OBJECTPROPERTIES_OF_SOURCE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_OBJECTPROPERTIES_OF_TARGET;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_OBJECTPROPERTY_SOURCES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_OBJECTPROPERTY_TARGETS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_PREFIXES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_SAME_INDIVIDUALS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_SETTINGS;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_SUBCLASSES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_SUBCLASS_HIERARCHY;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_SUBDATAPROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_SUBDATAPROPERTY_HIERARCHY;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_SUBOBJECTPROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_SUBOBJECTPROPERTY_HIERARCHY;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_SUPERCLASSES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_SUPERDATAPROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_SUPEROBJECTPROPERTIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.GET_TYPES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.IRI_MAPPING;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.IS_CLASS_SATISFIABLE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.IS_DATAPROPERTY_SATISFIABLE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.IS_ENTAILED;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.IS_ENTAILED_DIRECT;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.IS_KB_CONSISTENTLY_DECLARED;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.IS_KB_SATISFIABLE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.IS_OBJECTPROPERTY_SATISFIABLE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.KB_ATTRIBUTE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.KEY_ATTRIBUTE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.LITERAL;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.LOAD_ONTOLOGIES;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.NAME_Attribute;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.ONTOLOGY_IRI;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.PREFIX;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.REALIZE;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.RELEASE_KB;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.SET;
import static org.semanticweb.owlapi.owllink.OWLlinkXMLVocabulary.VALUE_ATTRIBUTE;
import org.semanticweb.owlapi.owllink.builtin.requests.Classify;
import org.semanticweb.owlapi.owllink.builtin.requests.CreateKB;
import org.semanticweb.owlapi.owllink.builtin.requests.GetAllAnnotationProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetAllClasses;
import org.semanticweb.owlapi.owllink.builtin.requests.GetAllDataProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetAllDatatypes;
import org.semanticweb.owlapi.owllink.builtin.requests.GetAllIndividuals;
import org.semanticweb.owlapi.owllink.builtin.requests.GetAllObjectProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDataPropertiesBetween;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDataPropertiesOfLiteral;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDataPropertiesOfSource;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDataPropertySources;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDataPropertyTargets;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDescription;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDifferentIndividuals;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDisjointClasses;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDisjointDataProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetDisjointObjectProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetEquivalentClasses;
import org.semanticweb.owlapi.owllink.builtin.requests.GetEquivalentDataProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetEquivalentObjectProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetFlattenedDataPropertySources;
import org.semanticweb.owlapi.owllink.builtin.requests.GetFlattenedDifferentIndividuals;
import org.semanticweb.owlapi.owllink.builtin.requests.GetFlattenedInstances;
import org.semanticweb.owlapi.owllink.builtin.requests.GetFlattenedObjectPropertySources;
import org.semanticweb.owlapi.owllink.builtin.requests.GetFlattenedObjectPropertyTargets;
import org.semanticweb.owlapi.owllink.builtin.requests.GetFlattenedTypes;
import org.semanticweb.owlapi.owllink.builtin.requests.GetInstances;
import org.semanticweb.owlapi.owllink.builtin.requests.GetKBLanguage;
import org.semanticweb.owlapi.owllink.builtin.requests.GetObjectPropertiesBetween;
import org.semanticweb.owlapi.owllink.builtin.requests.GetObjectPropertiesOfSource;
import org.semanticweb.owlapi.owllink.builtin.requests.GetObjectPropertiesOfTarget;
import org.semanticweb.owlapi.owllink.builtin.requests.GetObjectPropertySources;
import org.semanticweb.owlapi.owllink.builtin.requests.GetObjectPropertyTargets;
import org.semanticweb.owlapi.owllink.builtin.requests.GetPrefixes;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSameIndividuals;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSettings;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubClassHierarchy;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubClasses;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubDataProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubDataPropertyHierarchy;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubObjectProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSubObjectPropertyHierarchy;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSuperClasses;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSuperDataProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetSuperObjectProperties;
import org.semanticweb.owlapi.owllink.builtin.requests.GetTypes;
import org.semanticweb.owlapi.owllink.builtin.requests.IRIMapping;
import org.semanticweb.owlapi.owllink.builtin.requests.IsClassSatisfiable;
import org.semanticweb.owlapi.owllink.builtin.requests.IsDataPropertySatisfiable;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailed;
import org.semanticweb.owlapi.owllink.builtin.requests.IsEntailedDirect;
import org.semanticweb.owlapi.owllink.builtin.requests.IsKBConsistentlyDeclared;
import org.semanticweb.owlapi.owllink.builtin.requests.IsKBSatisfiable;
import org.semanticweb.owlapi.owllink.builtin.requests.IsObjectPropertySatisfiable;
import org.semanticweb.owlapi.owllink.builtin.requests.LoadOntologies;
import org.semanticweb.owlapi.owllink.builtin.requests.Realize;
import org.semanticweb.owlapi.owllink.builtin.requests.ReleaseKB;
import org.semanticweb.owlapi.owllink.builtin.requests.RequestVisitor;
import org.semanticweb.owlapi.owllink.builtin.requests.Set;
import org.semanticweb.owlapi.owllink.builtin.requests.Tell;

/**
 * @author Olaf Noppens
 */
public class BuiltinRequestRenderer implements OWLlinkXMLRequestRenderer, RequestVisitor {
    protected OWLlinkXMLWriter writer;


    public void answer(Classify query) {
        writer.writeStartElement(CLASSIFY.getURI());
        writer.writeKBAttribute(query.getKB());
        writer.writeEndElement();
    }

    public void answer(CreateKB query) {
        writer.writeStartElement(BuiltinRequestVocabulary.CREATEKB.getURI());
        if (query.getName() != null)
            writer.writeAttribute(NAME_Attribute.getURI().toString(), query.getName());
        if (query.getKB() != null)
            writer.writeAttribute(KB_ATTRIBUTE.getURI().toString(), query.getKB().toString());
        if (query.getPrefixes() != null) {
            final DefaultPrefixManager manager = new DefaultPrefixManager();
            final Map<String, String> prefixName2PrefixMap = manager.getPrefixName2PrefixMap();
            for (Map.Entry<String, String> prefix : query.getPrefixes().entrySet()) {
                writer.writeStartElement(PREFIX.getURI());
                writer.writeAttribute(NAME_Attribute.getURI().toString(), prefix.getKey().endsWith(":") ? prefix.getKey().substring(0, prefix.getKey().length() - 1) : prefix.getKey());
                writer.writeAttribute(FULLIRI.getURI().toString(), prefix.getValue());
                writer.writeEndElement();
                prefixName2PrefixMap.put(prefix.getValue(), prefix.getKey());
            }
            if (query.getKB() != null) {
                //if kb is known we can update prefix information

            }
        }
        writer.writeEndElement();
    }

    public void answer(GetAllAnnotationProperties query) {
        writer.writeStartElement(GET_ALL_ANNOTATION_PROPERTIES);
        writer.writeKBAttribute(query.getKB());
        writer.writeEndElement();
    }

    public void answer(GetAllClasses query) {
        writer.writeStartElement(GET_ALL_CLASSES);
        writer.writeKBAttribute(query.getKB());
        writer.writeEndElement();
    }

    public void answer(GetAllDataProperties query) {
        writer.writeStartElement(GET_ALL_DATAPROPERTIES);
        writer.writeKBAttribute(query.getKB());
        writer.writeEndElement();
    }

    public void answer(GetAllDatatypes query) {
        writer.writeStartElement(GET_ALL_DATATYPES);
        writer.writeKBAttribute(query.getKB());
        writer.writeEndElement();
    }

    public void answer(GetAllIndividuals query) {
        writer.writeStartElement(GET_ALL_INDIVIDUALS);
        writer.writeKBAttribute(query.getKB());
        writer.writeEndElement();
    }

    public void answer(GetAllObjectProperties query) {
        writer.writeStartElement(GET_ALL_OBJECTPROPERTIES);
        writer.writeKBAttribute(query.getKB());
        writer.writeEndElement();
    }

    public void answer(GetDataPropertiesBetween query) {
        writer.writeStartElement(GET_DATAPROPERTIES_BETWEEN);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeNegativeAttribute(query.isNegative());
        writer.writeOWLObject(query.getSourceIndividual(), kb);
        writer.writeOWLObject(query.getTargetValue(), kb);
        writer.writeEndElement();
    }

    public void answer(GetDataPropertiesOfLiteral query) {
        writer.writeStartElement(GET_DATAPROPERTIES_OF_LITERAL);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeNegativeAttribute(query.isNegative());
        writer.writeOWLObject(query.getTargetValue(), kb);
        writer.writeEndElement();
    }

    public void answer(GetDataPropertiesOfSource query) {
        writer.writeStartElement(GET_DATAPROPERTIES_OF_SOURCE);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeNegativeAttribute(query.isNegative());
        writer.writeOWLObject(query.getSourceIndividual(), kb);
        writer.writeEndElement();
    }

    public void answer(GetDataPropertySources query) {
        writer.writeStartElement(GET_DATAPROPERTY_SOURCES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeNegativeAttribute(query.isNegative());
        writer.writeOWLObject(query.getOWLProperty(), kb);
        writer.writeOWLObject(query.getTargetValue(), kb);
        writer.writeEndElement();
    }

    public void answer(GetDataPropertyTargets query) {
        writer.writeStartElement(GET_DATAPROPERTY_TARGETS);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getOWLProperty(), kb);
        writer.writeOWLObject(query.getSourceIndividual(), kb);
        writer.writeEndElement();
    }


    public void answer(GetDescription query) {
        writer.writeStartElement(GET_DESCRIPTION);
        writer.writeEndElement();
    }

    public void answer(GetDisjointClasses query) {
        writer.writeStartElement(GET_DISJOINT_CLASSES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getObject(), kb);
        writer.writeEndElement();
    }

    public void answer(GetDisjointDataProperties query) {
        writer.writeStartElement(GET_DISJOINT_DATAPROPERTIES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getObject(), kb);
        writer.writeEndElement();
    }

    public void answer(GetDifferentIndividuals query) {
        writer.writeStartElement(GET_DIFFERENT_INDIVIDUALS);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getObject(), kb);
        writer.writeEndElement();
    }

    public void answer(GetDisjointObjectProperties query) {
        writer.writeStartElement(GET_DISJOINT_OBJECTPROPERTIES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getObject(), kb);
        writer.writeEndElement();
    }

    public void answer(GetEquivalentClasses query) {
        writer.writeStartElement(GET_EQUIVALENT_CLASSES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getObject(), kb);
        writer.writeEndElement();
    }

    public void answer(GetEquivalentDataProperties query) {
        writer.writeStartElement(GET_EQUIVALENT_DATAPROPERTIES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getObject(), kb);
        writer.writeEndElement();
    }

    public void answer(GetSameIndividuals query) {
        writer.writeStartElement(GET_SAME_INDIVIDUALS);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getObject(), kb);
        writer.writeEndElement();
    }

    public void answer(GetEquivalentObjectProperties query) {
        writer.writeStartElement(GET_EQUIVALENT_OBJECTPROPERTIES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getObject(), kb);
        writer.writeEndElement();
    }

    public void answer(GetFlattenedDataPropertySources query) {
        writer.writeStartElement(GET_FLATTENED_DATAPROPERTY_SOURCES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeNegativeAttribute(query.isNegative());
        writer.writeOWLObject(query.getOWLProperty(), kb);
        writer.writeOWLObject(query.getTargetValue(), kb);
        writer.writeEndElement();
    }

    public void answer(GetFlattenedDifferentIndividuals query) {
        writer.writeStartElement(GET_FLATTENED_DIFFERENT_INDIVIDUALS);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getIndividual(), kb);
        writer.writeEndElement();
    }


    public void answer(GetFlattenedInstances query) {
        writer.writeStartElement(GET_FLATTENED_INSTANCES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeDirectAttribute(query.isDirect());
        writer.writeOWLObject(query.getClassExpression(), kb);
        writer.writeEndElement();
    }

    public void answer(GetFlattenedObjectPropertySources query) {
        writer.writeStartElement(GET_FLATTENED_OBJECTPROPERTY_SOURCES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeNegativeAttribute(query.isNegative());
        writer.writeOWLObject(query.getOWLProperty(), kb);
        writer.writeOWLObject(query.getOWLIndividual(), kb);
        writer.writeEndElement();
    }

    public void answer(GetFlattenedObjectPropertyTargets query) {
        writer.writeStartElement(GET_FLATTENED_OBJECTPROPERTY_TARGETS);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getOWLProperty(), kb);
        writer.writeOWLObject(query.getOWLIndividual(), kb);
        writer.writeEndElement();
    }

    public void answer(GetFlattenedTypes query) {
        writer.writeStartElement(GET_FLATTENED_TYPES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeDirectAttribute(query.isDirect());
        writer.writeOWLObject(query.getIndividual(), kb);
        writer.writeEndElement();
    }

    public void answer(GetInstances query) {
        writer.writeStartElement(GET_INSTANCES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeDirectAttribute(query.isDirect());
        writer.writeOWLObject(query.getClassExpression(), kb);
        writer.writeEndElement();
    }

    public void answer(GetKBLanguage query) {
        writer.writeStartElement(GET_KB_LANGUAGE);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeEndElement();
    }

    public void answer(GetObjectPropertiesBetween query) {
        writer.writeStartElement(GET_OBJECTPROPERTIES_BETWEEN);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeNegativeAttribute(query.isNegative());
        writer.writeOWLObject(query.getSourceIndividual(), kb);
        writer.writeOWLObject(query.getTargetIndividual(), kb);
        writer.writeEndElement();
    }

    public void answer(GetObjectPropertiesOfSource query) {
        writer.writeStartElement(GET_OBJECTPROPERTIES_OF_SOURCE);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeNegativeAttribute(query.isNegative());
        writer.writeOWLObject(query.getIndividual(), kb);
        writer.writeEndElement();
    }

    public void answer(GetObjectPropertiesOfTarget query) {
        writer.writeStartElement(GET_OBJECTPROPERTIES_OF_TARGET);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeNegativeAttribute(query.isNegative());
        writer.writeOWLObject(query.getIndividual(), kb);
        writer.writeEndElement();
    }

    public void answer(GetObjectPropertySources query) {
        writer.writeStartElement(GET_OBJECTPROPERTY_SOURCES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeNegativeAttribute(query.isNegative());
        writer.writeOWLObject(query.getOWLProperty(), kb);
        writer.writeOWLObject(query.getOWLIndividual(), kb);
        writer.writeEndElement();
    }

    public void answer(GetObjectPropertyTargets query) {
        writer.writeStartElement(GET_OBJECTPROPERTY_TARGETS);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeNegativeAttribute(query.isNegative());
        writer.writeOWLObject(query.getOWLProperty(), kb);
        writer.writeOWLObject(query.getOWLIndividual(), kb);
        writer.writeEndElement();
    }

    public void answer(GetPrefixes query) {
        writer.writeStartElement(GET_PREFIXES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeEndElement();
    }

    public void answer(GetSettings query) {
        writer.writeStartElement(GET_SETTINGS);
        writer.writeKBAttribute(query.getKB());
        writer.writeEndElement();
    }

    public void answer(GetSubClasses query) {
        writer.writeStartElement(GET_SUBCLASSES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeDirectAttribute(query.isDirect());
        writer.writeOWLObject(query.getClassExpression(), kb);
        writer.writeEndElement();
    }

    public void answer(GetSubClassHierarchy query) {
        writer.writeStartElement(GET_SUBCLASS_HIERARCHY);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        if (query.getOWLClass() != null)
            writer.writeOWLObject(query.getOWLClass(), kb);
        writer.writeEndElement();
    }

    public void answer(GetSubDataProperties query) {
        writer.writeStartElement(GET_SUBDATAPROPERTIES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeDirectAttribute(query.isDirect());
        writer.writeOWLObject(query.getProperty(), kb);
        writer.writeEndElement();
    }

    public void answer(GetSubDataPropertyHierarchy query) {
        writer.writeStartElement(GET_SUBDATAPROPERTY_HIERARCHY);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        if (query.getOWLProperty() != null)
            writer.writeOWLObject(query.getOWLProperty(), kb);
        writer.writeEndElement();
    }

    public void answer(GetSubObjectProperties query) {
        writer.writeStartElement(GET_SUBOBJECTPROPERTIES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeDirectAttribute(query.isDirect());
        writer.writeOWLObject(query.getOWLObjectPropertyExpression(), kb);
        writer.writeEndElement();
    }

    public void answer(GetSubObjectPropertyHierarchy query) {
        writer.writeStartElement(GET_SUBOBJECTPROPERTY_HIERARCHY);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        if (query.getObjectProperty() != null)
            writer.writeOWLObject(query.getObjectProperty(), kb);
        writer.writeEndElement();
    }


    public void answer(GetSuperClasses query) {
        writer.writeStartElement(GET_SUPERCLASSES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeDirectAttribute(query.isDirect());
        writer.writeOWLObject(query.getOWLClassExpression(), kb);
        writer.writeEndElement();
    }

    public void answer(GetSuperDataProperties query) {
        writer.writeStartElement(GET_SUPERDATAPROPERTIES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeDirectAttribute(query.isDirect());
        writer.writeOWLObject(query.getProperty(), kb);
        writer.writeEndElement();
    }

    public void answer(GetSuperObjectProperties query) {
        writer.writeStartElement(GET_SUPEROBJECTPROPERTIES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeDirectAttribute(query.isDirect());
        writer.writeOWLObject(query.getProperty(), kb);
        writer.writeEndElement();
    }

    public void answer(GetTypes query) {
        writer.writeStartElement(GET_TYPES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeDirectAttribute(query.isDirect());
        writer.writeOWLObject(query.getIndividual(), kb);
        writer.writeEndElement();
    }

    public void answer(IsClassSatisfiable query) {
        writer.writeStartElement(IS_CLASS_SATISFIABLE);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getObject(), kb);
        writer.writeEndElement();
    }

    public void answer(IsDataPropertySatisfiable query) {
        writer.writeStartElement(IS_DATAPROPERTY_SATISFIABLE);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getObject(), kb);
        writer.writeEndElement();
    }

    public void answer(IsKBConsistentlyDeclared query) {
        writer.writeStartElement(IS_KB_CONSISTENTLY_DECLARED);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeEndElement();
    }

    public void answer(IsKBSatisfiable query) {
        writer.writeStartElement(IS_KB_SATISFIABLE);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeEndElement();
    }

    public void answer(IsEntailed query) {
        writer.writeStartElement(IS_ENTAILED);
        writer.writeKBAttribute(query.getKB());
        writer.writeOWLObject(query.getAxiom(), query.getKB());
        writer.writeEndElement();
    }

    public void answer(IsEntailedDirect query) {
        writer.writeStartElement(IS_ENTAILED_DIRECT);
        writer.writeKBAttribute(query.getKB());
        writer.writeOWLObject(query.getAxiom(), query.getKB());
        writer.writeEndElement();
    }

    public void answer(IsObjectPropertySatisfiable query) {
        writer.writeStartElement(IS_OBJECTPROPERTY_SATISFIABLE);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeOWLObject(query.getObject(), kb);
        writer.writeEndElement();
    }

    public void answer(LoadOntologies query) {
        writer.writeStartElement(LOAD_ONTOLOGIES);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        if (!query.isConsideringImports())
            writer.writeAttribute(CONSIDER_IMPORTS_ATTRIBUTE.getURI().toString(), Boolean.FALSE.toString());
        for (IRI ontologyIRI : query.getOntologyIRIs()) {
            writer.writeStartElement(ONTOLOGY_IRI);
            writer.writeFullIRIAttribute(ontologyIRI);
            writer.writeEndElement();
        }
        for (IRIMapping irimapping : query.getIRIMapping()) {
            writer.writeStartElement(IRI_MAPPING);
            writer.writeAttribute(KEY_ATTRIBUTE.getURI(), irimapping.key);
            writer.writeAttribute(VALUE_ATTRIBUTE.getURI(), irimapping.value.toString());
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    public void answer(Realize query) {
        writer.writeStartElement(REALIZE);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeEndElement();
    }

    public void answer(ReleaseKB query) {
        writer.writeStartElement(RELEASE_KB);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeEndElement();
    }

    public void answer(Set query) {
        writer.writeStartElement(SET);
        final IRI kb = query.getKB();
        writer.writeKBAttribute(kb);
        writer.writeAttribute(KEY_ATTRIBUTE.getURI(), query.getKey());
        for (OWLlinkLiteral literal : query) {
            writer.writeStartElement(LITERAL);
            writer.writeTextContent(literal.getValue());
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    public void answer(Tell request) {
        writer.writeStartElement(BuiltinRequestVocabulary.TELL.getURI());
        IRI kb = request.getKB();
        writer.writeKBAttribute(kb);
        for (OWLAxiom axiom : request) {
            writer.writeOWLObject(axiom, kb);
        }
        writer.writeEndElement();
    }

    public void answer(Request request) {
    }


    public void render(Request request, OWLlinkXMLWriter writer) throws OWLRendererException {
        this.writer = writer;
        request.accept(this);
        //this.answer(request);
    }
}
