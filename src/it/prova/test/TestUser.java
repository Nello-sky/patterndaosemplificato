package it.prova.test;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import it.prova.connection.MyConnection;
import it.prova.dao.Constants;
import it.prova.dao.user.UserDAO;
import it.prova.dao.user.UserDAOImpl;
import it.prova.model.User;

public class TestUser {

	public static void main(String[] args) {

		UserDAO userDAOInstance = null;

		// ##############################################################################################################
		// Grande novità: la Connection viene allestista dal chiamante!!! Non è più a
		// carico dei singoli metodi DAO!!!
		// ##############################################################################################################
		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {
			// ecco chi 'inietta' la connection: il chiamante
			userDAOInstance = new UserDAOImpl(connection);

//			System.out.println("In tabella user ci sono " + userDAOInstance.list().size() + " elementi.");
//
//			testInsertUser(userDAOInstance);
//			System.out.println("In tabella user ci sono " + userDAOInstance.list().size() + " elementi.");
//
//			testFindById(userDAOInstance);
//
//			testDeleteUser(userDAOInstance);
//			System.out.println("In tabella user ci sono " + userDAOInstance.list().size() + " elementi.");
//
//			testFindAllWhereDateCreatedGreaterThan(userDAOInstance);
//			System.out.println("In tabella user ci sono " + userDAOInstance.list().size() + " elementi.");

			// ESERCIZIO SUCCESSIVO: implementare metodi mancanti nel DAO
			
//			testFindAllByCognome(userDAOInstance);
//			System.out.println(userDAOInstance.findAllByCognome("porto"));
			
//			testFindAllByLoginIniziaCon(userDAOInstance);
//			System.out.println(userDAOInstance.findAllByLoginIniziaCon("m"));
			
//			testFindByLoginAndPassword(userDAOInstance);
//			System.out.println(userDAOInstance.findByLoginAndPassword("aa25", "w10"));
			
//			testFindAllByPasswordIsNull(userDAOInstance);
//			System.err.println(userDAOInstance.findAllByPasswordIsNull());
			
			
			
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void testInsertUser(UserDAO userDAOInstance) throws Exception {
		System.out.println(".......testInsertUser inizio.............");
		int quantiElementiInseriti = userDAOInstance
				.insert(new User("pluto", "plutotto", "ppp@example.com", "password@01", LocalDate.now()));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testInsertUser : FAILED");

		System.out.println(".......testInsertUser fine: PASSED.............");
	}

	private static void testFindById(UserDAO userDAOInstance) throws Exception {
		System.out.println(".......testFindById inizio.............");
		List<User> elencoVociPresenti = userDAOInstance.list();
		if (elencoVociPresenti.size() < 1)
			throw new RuntimeException("testFindById : FAILED, non ci sono voci sul DB");

		User primoDellaLista = elencoVociPresenti.get(0);

		User elementoCheRicercoColDAO = userDAOInstance.get(primoDellaLista.getId());
		if (elementoCheRicercoColDAO == null || !elementoCheRicercoColDAO.getLogin().equals(primoDellaLista.getLogin()))
			throw new RuntimeException("testFindById : FAILED, le login non corrispondono");

		System.out.println(".......testFindById fine: PASSED.............");
	}

	private static void testDeleteUser(UserDAO userDAOInstance) throws Exception {
		System.out.println(".......testDeleteUser inizio.............");
		// me ne creo uno al volo
		int quantiElementiInseriti = userDAOInstance
				.insert(new User("Giuseppe", "Verdi", "g.verdi@example.com", "password@01", LocalDate.now()));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testDeleteUser : FAILED, user da rimuovere non inserito");

		List<User> elencoVociPresenti = userDAOInstance.list();
		int numeroElementiPresentiPrimaDellaRimozione = elencoVociPresenti.size();
		if (numeroElementiPresentiPrimaDellaRimozione < 1)
			throw new RuntimeException("testDeleteUser : FAILED, non ci sono voci sul DB");

		User ultimoDellaLista = elencoVociPresenti.get(numeroElementiPresentiPrimaDellaRimozione - 1);
		userDAOInstance.delete(ultimoDellaLista);

		// ricarico per vedere se sono scalati di una unità
		int numeroElementiPresentiDopoDellaRimozione = userDAOInstance.list().size();
		if (numeroElementiPresentiDopoDellaRimozione != numeroElementiPresentiPrimaDellaRimozione - 1)
			throw new RuntimeException("testDeleteUser : FAILED, la rimozione non è avvenuta");

		System.out.println(".......testDeleteUser fine: PASSED.............");
	}

	private static void testFindAllWhereDateCreatedGreaterThan(UserDAO userDAOInstance) throws Exception {
		System.out.println(".......testFindAllWhereDateCreatedGreaterThan inizio.............");

		LocalDate dataCreazione = LocalDate.parse("2022-02-02");
		LocalDate dataCreazioneIlGiornoPrima = LocalDate.parse("2022-01-02");

		// me ne creo un paio che fanno al caso mio così almeno due li troverò
		User marioRossi = new User("Mario", "Rossi", "m.rossi@example.com", "password@01", dataCreazione);
		User giuseppeBianchi = new User("Giuseppe", "Bianchi", "g.bianchi@example.com", "password@01", dataCreazione);

		int quantiElementiInseriti = userDAOInstance.insert(marioRossi);
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testFindAllWhereDateCreatedGreaterThan : FAILED, user non inserito");

		quantiElementiInseriti = userDAOInstance.insert(giuseppeBianchi);
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testFindAllWhereDateCreatedGreaterThan : FAILED, user non inserito");

		// ora provo ad estrarli e devono avere tutti data successiva a quella scelta
		List<User> elencoVociCreateDopoDataScelta = userDAOInstance
				.findAllWhereDateCreatedGreaterThan(dataCreazioneIlGiornoPrima);
		for (User userItem : elencoVociCreateDopoDataScelta) {
			if (userItem.getDateCreated().isBefore(dataCreazioneIlGiornoPrima))
				throw new RuntimeException(
						"testFindAllWhereDateCreatedGreaterThan : FAILED, user con data precedente con id: "
								+ userItem.getId());
		}

		System.out.println(".......testFindAllWhereDateCreatedGreaterThan fine: PASSED.............");
	}


	
	private static void testFindAllByCognome(UserDAO userDAOInstance) throws Exception {
		System.out.println(".......testFindAllByCognome inizio.............");
		
		List<User> elencoVociPresenti = userDAOInstance.list();
		if (elencoVociPresenti.size() < 1)
			throw new RuntimeException("testFindAllByCognome : FAILED, non ci sono voci sul DB");
		
		String cognomeInTable=elencoVociPresenti.get(0).getCognome();
		List<User> test = userDAOInstance.findAllByCognome(cognomeInTable);
		if (test.size() < 1)
			throw new RuntimeException("testFindAllByCognome : FAILED");

		System.out.println(".......testFindAllByCognome fine: PASSED.............");
	}
	
	private static void testFindAllByLoginIniziaCon(UserDAO userDAOInstance) throws Exception {
		System.out.println(".......testFindAllByLoginIniziaCon inizio.............");
		
		List<User> elencoVociPresenti = userDAOInstance.list();
		if (elencoVociPresenti.size() < 1)
			throw new RuntimeException("testFindAllByLoginIniziaCon : FAILED, non ci sono voci sul DB");
		
		String iniziale= String.valueOf(elencoVociPresenti.get(0).getNome().charAt(0)) ;
		List<User> test = userDAOInstance.findAllByLoginIniziaCon(iniziale);
		if (test.size() < 1)
			throw new RuntimeException("testFindAllByLoginIniziaCon : FAILED");

		System.out.println(".......testFindAllByLoginIniziaCon fine: PASSED.............");
	}
	
	private static void testFindByLoginAndPassword(UserDAO userDAOInstance) throws Exception {
		System.out.println(".......testFindByLoginAndPassword inizio.............");
		
		List<User> elencoVociPresenti = userDAOInstance.list();
		if (elencoVociPresenti.size() < 1)
			throw new RuntimeException("testFindByLoginAndPassword : FAILED, non ci sono voci sul DB");
		
		String LoginInTable=elencoVociPresenti.get(0).getLogin();
		String PasswordInTable=elencoVociPresenti.get(0).getPassword();
		User test = userDAOInstance.findByLoginAndPassword(LoginInTable,PasswordInTable);
		if (test == null)
			throw new RuntimeException("testFindByLoginAndPassword : FAILED");

		System.out.println(".......testFindByLoginAndPassword fine: PASSED.............");
	}
	
	private static void testFindAllByPasswordIsNull(UserDAO userDAOInstance) throws Exception {
		System.out.println(".......testFindAllByPasswordIsNull inizio.............");
		
		List<User> elencoVociPresenti = userDAOInstance.list();
		if (elencoVociPresenti.size() < 1)
			throw new RuntimeException("testFindAllByPasswordIsNull : FAILED, non ci sono voci sul DB");
		
		
		List<User> test = userDAOInstance.findAllByPasswordIsNull();
		if (test.size() < 1)
			throw new RuntimeException("testFindAllByPasswordIsNull : FAILED");

		System.out.println(".......testFindAllByPasswordIsNull fine: PASSED.............");
	}
	
	
}
