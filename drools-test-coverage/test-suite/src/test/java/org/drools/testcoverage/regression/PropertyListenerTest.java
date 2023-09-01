package org.drools.testcoverage.regression;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.testcoverage.common.util.KieBaseTestConfiguration;
import org.drools.testcoverage.common.util.KieBaseUtil;
import org.drools.testcoverage.common.util.TestConstants;
import org.drools.testcoverage.common.util.TestParametersUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Bugfix regression test for:
 * https://bugzilla.redhat.com/show_bug.cgi?id=746887
 * https://issues.jboss.org/browse/JBRULES-3256
 */
@RunWith(Parameterized.class)
public class PropertyListenerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyListenerTest.class);

    private final KieBaseTestConfiguration kieBaseTestConfiguration;

    public PropertyListenerTest(final KieBaseTestConfiguration kieBaseTestConfiguration) {
        this.kieBaseTestConfiguration = kieBaseTestConfiguration;
    }

    @Parameters
    public static Collection<Object[]> getParameters() {
        return TestParametersUtil.getKieBaseConfigurations();
    }

    @Test
    public void runTest() {
        final KieServices kieServices = KieServices.Factory.get();
        final Resource drlResource = kieServices.getResources().newClassPathResource("propertyListenerTest.drl", getClass());
        final KieBase kieBase = KieBaseUtil.getKieBaseFromKieModuleFromResources(TestConstants.PACKAGE_REGRESSION,
                                                                                 kieBaseTestConfiguration, drlResource);

        final KieSession session = kieBase.newKieSession();

        final ArrayList<Person> people = new ArrayList<Person>();
        people.add(new Person("Test 1"));
        people.add(new Person("Test 2"));

        LOGGER.info("== Listeners attached before rules ==");
        for (Person person : people) {
            for (PropertyChangeListener listener : person.getBoundSupport().getPropertyChangeListeners()) {
                LOGGER.info("Listener attached of type: " + listener);
            }
        }

        // call rules
        final List<Command<?>> commands = new ArrayList<Command<?>>();
        commands.add(kieServices.getCommands().newInsert(people));
        commands.add(kieServices.getCommands().newFireAllRules());
        session.execute(kieServices.getCommands().newBatchExecution(commands, null));
        session.dispose();

        LOGGER.info("Session disposed");
        LOGGER.info("== Listeners attached after rules (should be none) ==");
        printAndAssertListenersFromPeople(people);

        // update name to trigger update
        people.get(0).setName("Test 3");
        LOGGER.info("== Listeners attached after updating name (should be none) ==");
        printAndAssertListenersFromPeople(people);
    }

    private void printAndAssertListenersFromPeople(final Collection<Person> people) {
        for (Person person : people) {
            for (PropertyChangeListener listener : person.getBoundSupport().getPropertyChangeListeners()) {
                LOGGER.info("Listener attached of type: " + listener);
            }
            // there should be no listeners
            assertThat(person.getBoundSupport().getPropertyChangeListeners()).isEmpty();
        }
    }

    public static class Person {

        private String name;

        private PropertyChangeSupport boundSupport;

        public Person() {
            this(null);
        }

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String value) {
            String oldValue = this.name;
            this.name = value;
            PropertyChangeSupport support = this.getBoundSupport();
            support.firePropertyChange("name", oldValue, this.name);
        }

        public PropertyChangeSupport getBoundSupport() {
            if (null == this.boundSupport) {
                this.boundSupport = new PropertyChangeSupport(this);
            }
            return this.boundSupport;
        }

        public void removePropertyChangeListener(String param0, PropertyChangeListener param1) {
            this.getBoundSupport().removePropertyChangeListener(param0, param1);
        }

        public void addPropertyChangeListener(String param0, PropertyChangeListener param1) {
            this.getBoundSupport().addPropertyChangeListener(param0, param1);
        }

        public void removePropertyChangeListener(PropertyChangeListener param0) {
            this.getBoundSupport().removePropertyChangeListener(param0);
        }

        public void addPropertyChangeListener(PropertyChangeListener param0) {
            this.getBoundSupport().addPropertyChangeListener(param0);
        }

    }

}
