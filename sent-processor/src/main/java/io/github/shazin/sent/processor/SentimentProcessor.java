package io.github.shazin.sent.processor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.transformer.MessageTransformationException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Simple Sentiment Processor
 * 
 * @author shazin
 *
 */
@MessageEndpoint
public class SentimentProcessor {
	
	@Autowired
	private Properties ignoreWordsProperties;
	
	@Autowired
	private Properties negativeWordsProperties;

	private ObjectMapper objectMapper = new ObjectMapper();
	
	private Map<Character, Set<String>> negativeWordsIndex = new HashMap<Character, Set<String>>();
	
	private Map<Character, Set<String>> IgnoreWordsIndex = new HashMap<Character, Set<String>>();
	
	@PostConstruct
	public void init() {		
		try {
			loadProperties(negativeWordsProperties, negativeWordsIndex);
			loadProperties(ignoreWordsProperties, IgnoreWordsIndex);
		} catch(Exception e) {
			throw new RuntimeException("Can not load negativewords.index and/or ignorewords.index");
		}		
	}

	private void loadProperties(Properties properties , Map<Character, Set<String>> map) throws IOException {
		for(Map.Entry<Object, Object> entry:properties.entrySet()) {
			map.put(entry.getKey().toString().toLowerCase().charAt(0), new HashSet<String>(Arrays.asList(entry.getValue().toString().split(","))));
		}
	}
	
	@Transformer(inputChannel = "input", outputChannel = "output")
	public String transform(String tweet) {
		try {
			Map<String, Object> json = objectMapper.readValue(tweet, new TypeReference<Map<String, Object>>() {});
			String text = json.get("text").toString();
			return String.format("%d", sentimentScore(text));
		} catch(Exception e) {
			throw new MessageTransformationException("Unable to transform message : "+e.toString());
		}
	}
	
	public int sentimentScore(String text) {
		int negative = 0;
		StringTokenizer tokenizer = new StringTokenizer(text, " ");
		while(tokenizer.hasMoreTokens()) {
			String word = tokenizer.nextToken().trim().toLowerCase();
			if(!word.isEmpty()) {
				Set<String> ignoreWords = IgnoreWordsIndex.get(word.charAt(0));
				if(ignoreWords != null && ignoreWords.contains(word)) {
					continue;
				}
				Set<String> negativeWords = negativeWordsIndex.get(word.charAt(0));
				if(negativeWords != null && negativeWords.contains(word)) {
					negative++;					
				}
			}
		}
		return negative > 0 ? -1 : 1;  		
	}
}
