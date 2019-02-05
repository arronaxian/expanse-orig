package com.ds.expanse.app.nlp;

import java.util.ArrayList;
import java.util.List;

public class SynonymDO {
    private List<String> synonyms = new ArrayList<>();
    private List<String> modifiers = new ArrayList<>();

    private String command;
    private String type;

    public SynonymDO() {
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
