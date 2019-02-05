package com.ds.expanse.app.nlp;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ds.expanse.app.api.command.Command;
import com.ds.expanse.app.command.TransitionCommand;
import com.ds.expanse.app.command.UnknownCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;


public class ExpanseNLP {
    private static SynonymsDO SYNONYMS;

    private final static Map<String, Command> COMMANDS = new HashMap<>();

    static {
        COMMANDS.put("unknown", new UnknownCommand("unknown"));
    }

    /**
     * Matches the user typed command with the command to perform the requested action.
     * @param userCommand The user typed command.
     * @return The Command, otherwise an instance of the UnknownCommand.
     */
    public Command matchCommand(String userCommand) {
        // Make everything lowercase
        userCommand = userCommand.trim().toLowerCase();

        // Did we get lucky?
        Command foundCommand = COMMANDS.get(userCommand);

        // Simple non-nlp matching algorithm
        if ( foundCommand == null ) {
            String[] splits = userCommand.split(" ");
            for ( String word : splits) {
                foundCommand = COMMANDS.get(word.trim().toLowerCase());
                if ( foundCommand != null ) {
                    break;
                }
            }
        }

        if ( foundCommand == null ) {
            foundCommand = new UnknownCommand("unknown");
        }

        return foundCommand;
    }

    /**
     * Get mapped command by name.
     * @param commandName The command name.
     * @return The mapped command, otherwise null.
     */
    public Command getCommand(String commandName) {
        return COMMANDS.get(commandName);
    }

    /**
     * Maps the command instance to the command name.
     * @param commandName The command name.
     * @param command The mapped command instance.
     */
    public void mapCommand(String commandName, Command command) {
        COMMANDS.put(commandName, command);
    }

    public boolean allow(String command) throws IOException {
        String[] sentences = getSentences(command);

        //        //Instantiating SimpleTokenizer class
        SimpleTokenizer simpleTokenizer = SimpleTokenizer.INSTANCE;

        //Tokenizing the given sentence

        List<Command> commands =
            Stream.of(sentences)
                    .map(s ->
                    {
                        String tokens[] = simpleTokenizer.tokenize(s);

                        // Filter out all words not relating to a command.
//                        Stream.of(tokens).filter(t -> { if ( SYNONYMS.)})

                        return new TransitionCommand("");

                    })
                    .collect(Collectors.toList());



//        InputStream inputStream = this.getClass().getResourceAsStream("/en-ner-person.bin");
//        TokenNameFinderModel model = new TokenNameFinderModel(inputStream);
//
//        //Instantiating the NameFinder class
//        NameFinderME nameFinder = new NameFinderME(model);
//
//        //Getting the sentence in the form of String array
//        String [] sentence = new String[]{
//                "Mike",
//                "and",
//                "Smith",
//                "are",
//                "good",
//                "friends"
//        };
//
//        //Finding the names in the sentence
//        Span nameSpans[] = nameFinder.find(sentence);
//
//        //Printing the spans of the names in the sentence
//        for(Span s: nameSpans)
//            System.out.println(s.toString());

        return false;
    }

    public String[] getSentences(String command) throws IOException {
        //Loading sentence detector model
        InputStream inputStream = ExpanseNLP.class.getResourceAsStream("/en-sent.bin");
        SentenceModel model = new SentenceModel(inputStream);

        //Instantiating the SentenceDetectorME class
        SentenceDetectorME detector = new SentenceDetectorME(model);

        //Detecting the sentence
        String sentences[] = detector.sentDetect(command);

        return sentences;
    }

    public void load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream roomsStream = getClass().getResourceAsStream("/synonyms.json");
        SYNONYMS = mapper.readerFor(SynonymsDO.class).readValue(roomsStream);
    }

    public static void main(String args[]) throws Exception {
        new ExpanseNLP().allow("Grab the dagger. go north.");
        new ExpanseNLP().allow("Pick up the dagger Then go north.");

//        String sentence = "Hi. How are you? Welcome to Tutorialspoint. "
//                + "We provide free tutorials on various technologies";
//
//        //Loading sentence detector model
//        InputStream inputStream = ExpanseNLP.class.getResourceAsStream("/en-sent.bin");
//        SentenceModel model = new SentenceModel(inputStream);
//
//        //Instantiating the SentenceDetectorME class
//        SentenceDetectorME detector = new SentenceDetectorME(model);
//
//        //Detecting the sentence
//        String sentences[] = detector.sentDetect(sentence);
//
//        //Printing the sentences
//        for(String sent : sentences)
//            System.out.println(sent);
//
//
//        //Instantiating SimpleTokenizer class
//        SimpleTokenizer simpleTokenizer = SimpleTokenizer.INSTANCE;
//
//        //Tokenizing the given sentence
//        String tokens[] = simpleTokenizer.tokenize(sentence);
//
//        //Printing the tokens
//        for(String token : tokens) {
//            System.out.println(token);
//        }
    }

}
