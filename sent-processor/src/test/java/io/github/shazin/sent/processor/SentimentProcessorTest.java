package io.github.shazin.sent.processor;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.xd.dirt.server.SingleNodeApplication;
import org.springframework.xd.dirt.test.SingleNodeIntegrationTestSupport;
import org.springframework.xd.dirt.test.SingletonModuleRegistry;
import org.springframework.xd.dirt.test.process.SingleNodeProcessingChain;
import org.springframework.xd.module.ModuleType;
import org.springframework.xd.test.RandomConfigurationSupport;

import static org.junit.Assert.*;

/**
 * Sentiment Processor Test
 * 
 * @author shazin
 *
 */
public class SentimentProcessorTest {

	private static SingleNodeApplication application;

	private static int RECEIVE_TIMEOUT = 5000;

	private static String moduleName = "sent-processor";

	@BeforeClass
	public static void setUp() {
		RandomConfigurationSupport randomConfigSupport = new RandomConfigurationSupport();
		application = new SingleNodeApplication().run();
		SingleNodeIntegrationTestSupport singleNodeIntegrationTestSupport = new SingleNodeIntegrationTestSupport(
				application);
		singleNodeIntegrationTestSupport
				.addModuleRegistry(new SingletonModuleRegistry(
						ModuleType.processor, moduleName));

	}

	@Test
	public void test() {
		String streamName = "tweetTest";
		String tweet = "{\"text\":\"Shazin is a bad boy\"}"; // JSON omitted here for
														// clarity

		String processingChainUnderTest = moduleName;

		SingleNodeProcessingChain chain = new SingleNodeProcessingChain(
				application, streamName, processingChainUnderTest);

		chain.sendPayload(tweet);

		String result = (String) chain.receivePayload(RECEIVE_TIMEOUT);

		assertEquals("-1", result);

		// Unbind the source and sink channels from the message bus
		chain.destroy();
	}
}
