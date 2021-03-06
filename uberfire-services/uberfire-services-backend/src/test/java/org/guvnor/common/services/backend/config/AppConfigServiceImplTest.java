/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.guvnor.common.services.backend.config;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.inject.Instance;

import org.guvnor.common.services.backend.preferences.ApplicationPreferencesLoader;
import org.guvnor.common.services.backend.preferences.SystemPropertiesInitializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.mocks.MockInstanceImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppConfigServiceImplTest {

    @Mock
    private ApplicationPreferencesLoader preferencesLoader;
    private Instance<ApplicationPreferencesLoader> preferencesLoaders;

    @Mock
    private SystemPropertiesInitializer systemPropertiesInitializer;
    private Instance<SystemPropertiesInitializer> systemPropertiesInitializers;

    private AppConfigServiceImpl appConfigService;

    @Before
    public void setup() {
        preferencesLoaders = new MockInstanceImpl<>(preferencesLoader);
        systemPropertiesInitializers = new MockInstanceImpl<>(systemPropertiesInitializer);

        appConfigService = new AppConfigServiceImpl(preferencesLoaders,
                                                    systemPropertiesInitializers);
    }

    @Test
    public void systemPropertiesHavePriorityOnPreferencesLoaders() {
        final Map<String, String> loaderProperties = new HashMap<>();
        loaderProperties.put("property1", "loaderProperty1");
        loaderProperties.put("property2", "loaderProperty2");
        doReturn(loaderProperties).when(preferencesLoader).load();

        System.setProperty("property1", "systemProperty1");
        System.setProperty("property3", "systemProperty3");

        final Map<String, String> preferences = appConfigService.loadPreferences();

        assertEquals(2, preferences.size());
        assertEquals("systemProperty1", preferences.get("property1"));
        assertEquals("loaderProperty2", preferences.get("property2"));

        verify(systemPropertiesInitializer).setSystemProperties(preferences);
    }

}
