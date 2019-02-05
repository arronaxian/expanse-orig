package com.ds.expanse.app.nlp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynonymsDO {
    private List<SynonymDO> synonyms = new ArrayList<>();
    private Map<String, SynonymDO> lookup = new HashMap<>();

    public SynonymsDO() {
    }

    public List<SynonymDO> getSynonyms() {
        return synonyms;
    }

    public void setSynonym(List<SynonymDO> synonyms) {
        this.synonyms = synonyms;
    }

    @JsonIgnore
    public SynonymDO lookupSynonym(String word) {
        if ( lookup.isEmpty() ) {
            synonyms.forEach(syn -> {
                syn.getSynonyms().forEach(s -> {
                    lookup.put(s, syn);
                });
            });
        }

        return lookup.get(word);
    }

}
