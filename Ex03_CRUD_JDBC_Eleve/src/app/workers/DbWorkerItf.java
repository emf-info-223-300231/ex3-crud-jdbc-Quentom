package app.workers;

import app.beans.Personne;
import app.exceptions.MyDBException;
import java.util.List;

public interface DbWorkerItf {

  void connecterBdMySQL( String nomDB ) throws MyDBException;
  void connecterBdHSQLDB( String nomDB ) throws MyDBException;
  void connecterBdAccess( String nomDB ) throws MyDBException;
  public List<Personne> lirePersonnes() throws MyDBException;
  public Personne lire(int i) throws MyDBException;
  void deconnecter() throws MyDBException;
  public void creer(Personne p) throws MyDBException;
  public void modifier(Personne p) throws MyDBException;
  public void effacer(Personne p) throws MyDBException;
}
