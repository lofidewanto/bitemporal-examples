/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.crowdcode.bitemporal.example;

import org.joda.time.Interval;

import com.anasoft.os.daofusion.bitemporal.Bitemporal;
import com.anasoft.os.daofusion.bitemporal.BitemporalWrapper;

/**
 * BitemporalAdresse implementation.
 * 
 * @author Lofi Dewanto
 * @since 1.0.0
 * @version 1.0.0
 */
public class BitemporalAdresseImpl extends BitemporalWrapper<Adresse> implements BitemporalAdresse {

	private static final long serialVersionUID = -3045504986806681059L;

	@Override
	public Adresse getAdresse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAdresse(Adresse adresse) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setValue(Adresse value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Adresse getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bitemporal copyWith(Interval validityInterval) {
		// TODO Auto-generated method stub
		return null;
	}
}
