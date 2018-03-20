package be.pxl.student;

import org.junit.*;


public class BudgetPlannerTest {

	BudgetPlanner budgetPlanner;

	@Before
	public void setUp() throws Exception {

		budgetPlanner = new BudgetPlanner();
		// ... setup database connection
		// ... create transaction
		// ... feed some test data

	}

	@Ignore
	@Test
	public void feed_the_database() throws Exception {

		BudgetPlannerFeeder feeder = new BudgetPlannerFeeder();
		feeder.feedRandomData();

	}

	@Test
	public void test_get_payments_by_account_id() throws Exception {

		// ... List<Payment> = budgetPlanner.getPaymentsById ();

	}

	@Test
	public void test_create_label() throws Exception {

		// ... Label createdLabel = budgetPlanner.createLabel ();

	}

	@After
	public void tearDown() throws Exception {

		// ... rollback transaction
		// ... close connection

	}
}
