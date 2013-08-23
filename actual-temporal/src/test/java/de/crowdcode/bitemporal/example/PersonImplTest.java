package de.crowdcode.bitemporal.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PersonImplTest {

	@Mock
	private AddressRepository addressRepository;

	@InjectMocks
	private final PersonImpl personImpl = new PersonImpl();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetSingleAddressNoAddressesAvailable() {
		// Prepare
		Date currentDate = new Date();
		Collection<AddressImpl> addresses = new ArrayList<AddressImpl>();
		when(
				addressRepository.findByPersonIdAndValidity(anyLong(),
						eq(currentDate))).thenReturn(addresses);

		// CUT
		Address address = personImpl.getSingleAddress(currentDate);

		// Asserts
		assertNull(address);
	}

	@Test
	public void testGetSingleAddressOneAddressesAvailable() {
		// Prepare
		Date currentDate = new Date();
		AddressImpl firstAddress = new AddressImpl();
		firstAddress.setStreet("Hauptstr. 21");
		firstAddress.setCity("Koeln");
		firstAddress.setCode("50698");
		firstAddress.setValidFrom(currentDate);
		Collection<AddressImpl> addresses = new ArrayList<AddressImpl>();
		addresses.add(firstAddress);

		when(
				addressRepository.findByPersonIdAndValidity(anyLong(),
						eq(currentDate))).thenReturn(addresses);

		// CUT
		Address address = personImpl.getSingleAddress(currentDate);

		// Asserts
		assertEquals("Koeln", address.getCity());
	}

	@Test
	public void testGetSingleAddressMoreThanOneAddressesAvailable() {
		// Prepare
		Date currentDate = new Date();
		AddressImpl firstAddress = new AddressImpl();
		firstAddress.setStreet("Hauptstr. 21");
		firstAddress.setCity("Koeln");
		firstAddress.setCode("50698");
		// Valid from 9.9.1999
		Calendar cal = Calendar.getInstance();
		cal.set(1999, Calendar.SEPTEMBER, 9);
		firstAddress.setValidFrom(cal.getTime());

		AddressImpl secondAddress = new AddressImpl();
		secondAddress.setStreet("Grossmarkt 22");
		secondAddress.setCity("Berlin");
		secondAddress.setCode("10313");
		// Valid from 8.8.2008
		cal.set(2008, Calendar.AUGUST, 8);
		secondAddress.setValidFrom(cal.getTime());

		Collection<AddressImpl> addresses = new ArrayList<AddressImpl>();
		addresses.add(firstAddress);
		addresses.add(secondAddress);

		when(
				addressRepository.findByPersonIdAndValidity(anyLong(),
						eq(currentDate))).thenReturn(addresses);

		// CUT
		Address address = personImpl.getSingleAddress(currentDate);

		// Asserts
		assertEquals("Berlin", address.getCity());
	}
}
