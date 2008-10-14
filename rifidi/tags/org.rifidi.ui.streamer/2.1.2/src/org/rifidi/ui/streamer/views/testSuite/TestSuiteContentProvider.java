/**
 * 
 */
package org.rifidi.ui.streamer.views.testSuite;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.BatchSuite;
import org.rifidi.streamer.xml.ComponentSuite;
import org.rifidi.streamer.xml.ScenarioSuite;
import org.rifidi.streamer.xml.TestUnitSuite;
import org.rifidi.ui.common.registry.RegistryChangeListener;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TestSuiteContentProvider implements ITreeContentProvider,
		RegistryChangeListener {

	private Log logger = LogFactory.getLog(TestSuiteContentProvider.class);
	private TreeViewer viewer;
	private boolean scenariosWereEmpty = false;
	private boolean componentsWereEmpty = false;
	private boolean batchesWereEmpty = false;
	private boolean testUnitsWereEmpty = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		logger.debug("Try to get content of "
				+ parentElement.getClass().getSimpleName());
		if (parentElement instanceof InputObjectRegistry) {
			InputObjectRegistry registry = (InputObjectRegistry) parentElement;
			return getInputObjectRegistryChilds(registry);
		}
		if (parentElement instanceof BatchSuite) {
			return ((BatchSuite) parentElement).getBatches().toArray();
		}
		if (parentElement instanceof ComponentSuite) {
			return ((ComponentSuite) parentElement).getReaderComponents()
					.toArray();
		}
		if (parentElement instanceof ScenarioSuite) {
			return ((ScenarioSuite) parentElement).getScenarios().toArray();
		}
		if (parentElement instanceof TestUnitSuite) {
			return ((TestUnitSuite) parentElement).getTestUnits().toArray();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof InputObjectRegistry)
			return true;
		if (element instanceof BatchSuite)
			return true;
		if (element instanceof ComponentSuite)
			return true;
		if (element instanceof ScenarioSuite)
			return true;
		if (element instanceof TestUnitSuite)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof InputObjectRegistry) {
			InputObjectRegistry registry = (InputObjectRegistry) inputElement;
			return getInputObjectRegistryChilds(registry);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) viewer;
		if (oldInput != null) {
			((InputObjectRegistry) oldInput).removeListener(this);
		}
		if (newInput != null) {
			((InputObjectRegistry) newInput).addListener(this);
		}
	}

	/**
	 * Get all children of the InputObjectRegistry
	 * 
	 * @param registry
	 * @return
	 */
	private Object[] getInputObjectRegistryChilds(InputObjectRegistry registry) {
		ArrayList<Object> ret = new ArrayList<Object>();
		ScenarioSuite scenarioSuite = registry.getScenarioSuite();
		BatchSuite batchSuite = registry.getBatchSuite();
		ComponentSuite componentSuite = registry.getComponentSuite();
		// LoadTestSuite loadTestSuite = registry.getLoadTestSuite();
		TestUnitSuite testUnitSuite = registry.getTestUnitSuite();

		if (scenarioSuite != null) {
			ret.add(scenarioSuite);
		} else
			scenariosWereEmpty = true;
		if (batchSuite != null) {
			ret.add(batchSuite);
		} else
			batchesWereEmpty = true;
		if (componentSuite != null) {
			ret.add(componentSuite);
		} else
			componentsWereEmpty = true;
		if (testUnitSuite != null) {
			ret.add(testUnitSuite);
		} else
			testUnitsWereEmpty = true;
		return ret.toArray();
	}

	private boolean somethingWasEmpty() {
		return batchesWereEmpty || scenariosWereEmpty || componentsWereEmpty
				|| testUnitsWereEmpty;
	}

	@Override
	public void add(Object event) {
		if (event == null || somethingWasEmpty()) {
			logger.debug("Something was empty previously or event undefined");
			viewer.refresh();
		} else
			viewer.refresh(event, false);
	}

	@Override
	public void remove(Object event) {
		if (event != null || somethingWasEmpty())
			viewer.refresh();
		else
			viewer.refresh(event, false);
	}

	@Override
	public void update(Object event) {
		if (event != null || somethingWasEmpty())
			viewer.refresh();
		else
			viewer.refresh(event, false);
	}

}
