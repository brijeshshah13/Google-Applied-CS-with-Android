/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private int wordLength = DEFAULT_WORD_LENGTH;

    // Maps word length to an array list of all words of that length
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();

    // Holds dictionary
    ArrayList<String> wordList;

    // Used to verify if word is valid
    HashSet<String> wordSet;

    // Groups Anagrams together
    HashMap<String,ArrayList<String>> lettersToWord;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;

        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();

        while((line = in.readLine()) != null) {
            String word = line.trim();

            // Push back word to wordList
            wordList.add(word);

            // Push back word to wordSet
            wordSet.add(word);

            // START SIZE TO WORD
            if(sizeToWords.containsKey(word.length())) {
                // add current word to ArrayList at that key
                sizeToWords.get(word.length()).add(word);
            } else {
                // else, create a new ArrayList, add the word to it and store in
                // the HashMap with the corresponding key.
                ArrayList<String> allWords = new ArrayList<>();
                allWords.add(word);
                sizeToWords.put(word.length(), allWords);
            }
            // END SIZE TO WORD

            //Compares the entered word with already present keys in the HashMap
            if(lettersToWord.containsKey(sortLetters(word))){
                // Add current word to ArrayList at that key
                ArrayList<String> anagrams = lettersToWord.get(sortLetters(word));
                anagrams.add(word);
                lettersToWord.put(sortLetters(word),anagrams);
            }
            // Else, create a new ArrayList, add the word to it and store in
            // the HashMap with the corresponding key.
            else{
                ArrayList<String> anagrams = new ArrayList<>();
                anagrams.add(word);
                lettersToWord.put(sortLetters(word),anagrams);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        //word => word entered by user
        //base => the base word for which user has to form anagrams

        //Check if base is a substring of word
        if(word.contains(base))
            return false;

        //Check if word is valid
        if(wordSet.contains(word))
            return true;

        //Word is invalid
        return false;
    }

    // Sorts the letters of the input String
    public String sortLetters(String inputWord){
        char[] chars = inputWord.toCharArray();
        Arrays.sort(chars);
        String sortedWord = new String(chars);
        return  sortedWord;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        //Sort string of targetWord
        String sortedTargetedWord = sortLetters(targetWord);

        for (int i = 0; i < wordList.size(); i++){
            if(wordList.get(i).length() == targetWord.length()){

                //Sort string of the current index element of the wordList
                String sortedWordListWord = sortLetters(wordList.get(i));

                if(sortedTargetedWord.equalsIgnoreCase(sortedWordListWord))
                    result.add(wordList.get(i));
            }
        }
        //Comment the result for
        //return result;
        return lettersToWord.get(sortedTargetedWord);
    }

    // Takes a string and finds all anagrams that can be formed by adding one
    // letter to that word.
    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        for(char letter = 'a'; letter <= 'z'; letter++) {
            if (lettersToWord.containsKey(sortLetters(word + letter))) {
                // Get list of anagrams
                ArrayList<String> listAnagrams = lettersToWord.get(sortLetters(word + letter));
                // Add each anagram
                for (int i = 0; i < listAnagrams.size(); i++) {
                    if (isGoodWord(word, listAnagrams.get(i))) {
                        result.add(listAnagrams.get(i));
                    }
                }

            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        // Pick a random starting point in the wordList array and check each
        // word in the array until you find one that has at least MIN_NUM_ANAGRAMS anagrams.

        int randomNumber;
        int numAnagrams = 0;

        // Restrict your search to the words of length wordLength
        ArrayList<String> listWordsMaxLength = sizeToWords.get(wordLength);
        int arraySize = listWordsMaxLength.size();

        // While less than, keep picking new random word
        while (numAnagrams < MIN_NUM_ANAGRAMS) {

            // Get random number of size of array, then use it
            // to choose a (random) word from the fixed length array of words
            randomNumber = random.nextInt(arraySize);
            String randomWord = listWordsMaxLength.get(randomNumber);

            // Get size of array with all anagrams of randomly selected word
            // call getAnagramsWithOneMoreLetter since word.length() 3 on average contain 1 anagram max
            // creating infinite loop
            numAnagrams = getAnagramsWithOneMoreLetter(randomWord).size();
            if (numAnagrams >= MIN_NUM_ANAGRAMS) {
                if(wordLength < MAX_WORD_LENGTH) {
                    wordLength++;
                }
                return randomWord;
            }
        }
        return "stop";
    }
}
