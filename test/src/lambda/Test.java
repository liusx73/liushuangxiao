package lambda;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Test {
	public static void main(String[] args) {
		List<Person> roster = Person.createRoster();
		
		// printPersons(
		// roster, new CheckPersonEligibleForSelectiveService());

//		printPersons(roster, new CheckPerson() {
//			public boolean test(Person p) {
//				return p.getGender() == Person.Sex.MALE 
//						&& p.getAge() >= 18
//						&& p.getAge() <= 25;
//			}
//		});
//		printPersons(
//				roster, 
//				(Person p) -> p.getGender() == Person.Sex.MALE
//					&& p.getAge() >= 18
//					&& p.getAge() <= 25);
		/*printPersonsWithPredicate(
				roster,
				p -> p.getGender() == Person.Sex.MALE
					&& p.getAge() >= 18
					&& p.getAge() <= 25);*/
//		processPersons(
//				roster,
//				p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18
//						&& p.getAge() <= 25, p -> p.printPerson());
//		processPersonsWithFunction(
//				roster,
//				p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18
//						&& p.getAge() <= 25, 
//				p -> p.getEmailAddress(),
//				email -> System.out.println(email));
		roster.stream()
				.filter(p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25)
				.map(p -> p.getAge())
				.forEach(email -> System.out.println(email));
//		processElements(
//				roster,
//				p -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18
//						&& p.getAge() <= 25, 
//				p -> p.getEmailAddress(),
//				email -> System.out.println(email));
	}
	
	public static <X, Y> void processElements(Iterable<X> source,
			Predicate<X> tester, Function<X, Y> mapper, Consumer<Y> block) {
		for (X p : source) {
			if (tester.test(p)) {
				Y data = mapper.apply(p);
				block.accept(data);
			}
		}
	}

	public static void processPersonsWithFunction(List<Person> roster,
			Predicate<Person> tester, Function<Person, String> mapper,
			Consumer<String> block) {
		for (Person p : roster) {
			if (tester.test(p)) {
				String data = mapper.apply(p);
				block.accept(data);
			}
		}
	}
	public static void printPersonsOlderThan(List<Person> roster, int age) {
		for (Person p : roster) {
			if (p.getAge() >= age) {
				p.printPerson();
			}
		}
	}

	public static void printPersonsWithinAgeRange(List<Person> roster, int low,
			int high) {
		for (Person p : roster) {
			if (low <= p.getAge() && p.getAge() < high) {
				p.printPerson();
			}
		}
	}

	public static void printPersons(List<Person> roster, CheckPerson tester) {
		for (Person p : roster) {
			if (tester.test(p)) {
				p.printPerson();
			}
		}
	}

	public static void printPersonsWithPredicate(List<Person> roster,
			Predicate<Person> tester) {
		for (Person p : roster) {
			if (tester.test(p)) {
				p.printPerson();
			}
		}
	}

	public static void processPersons(List<Person> roster,
			Predicate<Person> tester, Consumer<Person> block) {
		for (Person p : roster) {
			if (tester.test(p)) {
				block.accept(p);
			}
		}
	}
}

interface CheckPerson {
	boolean test(Person p);
}

interface Predicate<T> {
	boolean test(T t);
}

class CheckPersonEligibleForSelectiveService implements CheckPerson {
	public boolean test(Person p) {
		return p.gender == Person.Sex.MALE && p.getAge() >= 18
				&& p.getAge() <= 25;
	}
}
