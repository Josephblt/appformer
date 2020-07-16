/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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

package org.uberfire.client.screens.welcome;

import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Composite;
import jsinterop.base.Js;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.views.pfly.monaco.MonacoEditorInitializer;
import org.uberfire.client.views.pfly.monaco.jsinterop.Monaco;
import org.uberfire.client.views.pfly.monaco.jsinterop.MonacoStandaloneCodeEditor;

@Dependent
@WorkbenchScreen(identifier = "WelcomeTabScreen")
@Templated
public class WelcomeTabScreen extends Composite {

    @Inject
    @DataField("monacoEditor")
    private Div monacoEditor;

    @WorkbenchPartTitle
    public String getTitle() {
        return "Welcome Tab 2";
    }

    @PostConstruct
    public void init() {
        MonacoEditorInitializer monacoEditorInitializer = new MonacoEditorInitializer();
        monacoEditorInitializer.require(onMonacoLoaded());
    }

    Consumer<Monaco> onMonacoLoaded() {
        return monaco -> {
            final JSONObject options = new JSONObject();
            options.put("value", new JSONString("private void javaTest() { }"));
            options.put("language", new JSONString("java"));

            /*This
            options.put("value", new JSONString("function javascriptTest() { }"));
            options.put("language", new JSONString("javascript"));
             */

            final MonacoStandaloneCodeEditor codeEditor = monaco.editor.create(Js.uncheckedCast(this.monacoEditor), options.getJavaScriptObject());
        };
    }
}
