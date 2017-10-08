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

package com.google.engedu.ghost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;
    private Random random = new Random();

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    /**
     * Add the new word in the Trie by appending at the appropriate
     * existing node or create a new node and than insert it
     * @param s
     */
    public void add(String s) {
        HashMap<String, TrieNode> temp = children;
        for (int i = 0; i < s.length(); i++){
            if (!temp.containsKey(String.valueOf(s.charAt(i)))){
                temp.put(String.valueOf(s.charAt(i)), new TrieNode());
            }
            if (i == s.length() - 1){
                temp.get(String.valueOf(s.charAt(i))).isWord = true;
            }
            temp = temp.get(String.valueOf(s.charAt(i))).children;
        }
    }

    /**
     * Search for the word starting with the input prefix
     * @param s
     * @return returns appropriate object of the TrieNode or null
     */
    private TrieNode searchNode(String s){
        TrieNode temp = this;
        for (int i = 0; i < s.length(); i++){
            if (temp.children.containsKey(String.valueOf(s.charAt(i)))){
                temp = temp.children.get(String.valueOf(s.charAt(i)));
            }else{
                return null;
            }
        }
        return temp;
    }

    public boolean isWord(String s) {
        TrieNode temp = searchNode(s);
        if (temp == null){
            return false;
        } else{
            return temp.isWord;
        }
    }

    /**
     * Computer plays simply by selecting the same word every time
     * obtained from the Binary Search
     * @param s
     * @return returns either the selected word or an appropriate string
     */
    public String getAnyWordStartingWith(String s) {
        TrieNode temp = searchNode(s);
        if (temp == null){
            return "noWord";
        }
        while (!temp.isWord){
            for (String c: temp.children.keySet()){
                temp = temp.children.get(c);
                s += c;
                break;
            }
        }
        return s;
    }

    /**
     * Computer plays smartly by selecting a random word from the
     * short listed possible words.
     * @param s
     * @return returns either the selected word or an appropriate string
     */
    public String getGoodWordStartingWith(String s) {
        String x = s;
        TrieNode temp = searchNode(s);
        if (temp == null){
            return "noWord";
        }
        // get a random word
        ArrayList<String> charsNoWord = new ArrayList<>();
        ArrayList<String> charsWord = new ArrayList<>();
        String c;

        while (true){
            charsNoWord.clear();
            charsWord.clear();
            for (String ch: temp.children.keySet()){
                if (temp.children.get(ch).isWord){
                    charsWord.add(ch);
                } else {
                    charsNoWord.add(ch);
                }
            }
            System.out.println("------>"+charsNoWord+" "+charsWord);
            if (charsNoWord.size() == 0){
                if(charsWord.size() == 0){
                    return "sameAsPrefix";
                }
                s += charsWord.get( random.nextInt(charsWord.size()) );
                break;
            } else {
                c = charsNoWord.get( random.nextInt(charsNoWord.size()) );
                s += c;
                temp = temp.children.get(c);
            }
        }
        if(x.equals(s)){
            return "sameAsPrefix";
        }else{
            return s;
        }
    }
}
