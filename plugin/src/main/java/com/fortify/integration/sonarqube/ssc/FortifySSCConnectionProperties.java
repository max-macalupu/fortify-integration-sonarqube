/*******************************************************************************
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the 
 * "Software"), to deal in the Software without restriction, including without 
 * limitation the rights to use, copy, modify, merge, publish, distribute, 
 * sublicense, and/or sell copies of the Software, and to permit persons to 
 * whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY 
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 ******************************************************************************/
package com.fortify.integration.sonarqube.ssc;

import java.util.List;

import org.sonar.api.PropertyType;
import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.batch.ScannerSide;
import org.sonar.api.ce.ComputeEngineSide;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.server.ServerSide;

/**
 * Connection factory used to access a {@link FortifySSCConnectionProperties} instance.
 * The connection instance is instantiated when this factory is instantiated, so
 * a single instance of this class will always return the same connection object.
 * 
 * @author Ruud Senden
 *
 */
@ScannerSide
@InstantiationStrategy(InstantiationStrategy.PER_BATCH)
@ServerSide
@ComputeEngineSide
public class FortifySSCConnectionProperties {
	/** SonarQube property holding the SSC URL */
	private static final String PRP_SSC_URL = "sonar.fortify.ssc.url";
	/** SonarQube property holding the SSC application version id or name */
	private static final String PRP_SSC_APP_VERSION = "sonar.fortify.ssc.appversion";
	
	private final Configuration config;
	
	/**
	 * Constructor for injecting configuration
	 * @param config
	 */
	public FortifySSCConnectionProperties(Configuration config) {
		this.config = config;
	}
	
	public String getSSCUrl() {
		return config.get(PRP_SSC_URL).orElse(null);
	}
	
	public String getApplicationVersionNameOrId() {
		return config.get(PRP_SSC_APP_VERSION).orElse(null);
	}
	
	public static final void addPropertyDefinitions(List<PropertyDefinition> propertyDefinitions) {
		propertyDefinitions.add(PropertyDefinition.builder(PRP_SSC_URL)
				.name("SSC URL")
				.description("URL used to connect to SSC (http[s]://<user>:<password>@<host>[:port]/ssc or http[s]://authToken:token@<host>[:port]/ssc)")
				.type(PropertyType.PASSWORD)
				.build());
		
		propertyDefinitions.add(PropertyDefinition.builder(PRP_SSC_APP_VERSION)
				.name("SSC Application Version")
				.description("SSC Application Version Id or Name (<application>:<version>).")
				.type(PropertyType.STRING)
				.onlyOnQualifiers(Qualifiers.PROJECT)
				.build());
	}
}