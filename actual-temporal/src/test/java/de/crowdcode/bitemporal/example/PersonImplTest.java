package de.crowdcode.bitemporal.example;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
}
