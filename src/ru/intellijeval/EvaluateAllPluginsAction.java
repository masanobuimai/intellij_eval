/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.intellijeval;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.Map;

import static ru.intellijeval.Util.saveAllFiles;

/**
 * @author DKandalov
 */
public class EvaluateAllPluginsAction extends AnAction {

	public EvaluateAllPluginsAction() {
		super("Run all plugins", "Run all plugins", Util.EVAL_ALL_ICON);
	}

	@Override public void actionPerformed(AnActionEvent event) {
		evaluateAllPlugins(event);
	}

	@Override public void update(AnActionEvent event) {
		event.getPresentation().setEnabled(!EvalComponent.pluginToPathMap().isEmpty());
	}

	private void evaluateAllPlugins(AnActionEvent event) {
		saveAllFiles();

		EvalErrorReporter errorReporter = new EvalErrorReporter();
		Evaluator evaluator = new Evaluator(errorReporter);

		for (Map.Entry<String, String> entry : EvalComponent.pluginToPathMap().entrySet()) {
			String pluginId = entry.getKey();
			String path = entry.getValue();
			evaluator.doEval(pluginId, path, event);
		}

		errorReporter.reportLoadingErrors(event);
		errorReporter.reportEvaluationExceptions(event);
	}
}
