/*******************************************************************************
 * (c) Copyright 2020 Micro Focus or one of its affiliates
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
package com.fortify.integration.sonarqube.sq76.scanner;

import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.scanner.sensor.ProjectSensor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import com.fortify.integration.sonarqube.common.IFortifyConnectionHelper;

/**
 * This 7.6-specific abstract {@link ProjectSensor} base class provides functionality 
 * for storing the scanner-side connection helper, and executing concrete sensor 
 * implementations only if an FoD connection is available and the sensor is active.
 * Contrary to the 6.7-specific implementation, implementations extending from this
 * based class are executed only once per project, instead of being executed separately
 * for every module.
 * 
 * @author Ruud Senden
 *
 */
public abstract class FortifySQ76AbstractProjectSensor<CH extends IFortifyConnectionHelper<?>> implements ProjectSensor {
	private static final Logger LOG = Loggers.get(FortifySQ76AbstractProjectSensor.class);
	private final CH connHelper;
	
	public FortifySQ76AbstractProjectSensor(CH connHelper) {
		this.connHelper = connHelper;
	}
	
	@Override
	public final void execute(SensorContext context) {
		if ( !connHelper.isConnectionAvailable() ) {
			LOG.info("Skipping sensor execution; FoD connection has not been configured");
		} else if ( !isActive(context) ) {
			LOG.info("Skipping sensor execution; sensor is not active");
		} else {
			_execute(context);
		}
	}
	
	protected abstract void _execute(SensorContext context);

	protected boolean isActive(SensorContext context) {
		return true;
	}

	public final CH getConnHelper() {
		return connHelper;
	}
}
