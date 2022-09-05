package app.workers;

import app.beans.Personne;
import app.exceptions.MyDBException;
import app.helpers.SystemLib;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DbWorker implements DbWorkerItf {

    private Connection dbConnexion;
    private List<Personne> listePersonnes;
    private int index = 0;

    /**
     * Constructeur du worker
     */
    public DbWorker() {
    }

    @Override
    public void connecterBdMySQL(String nomDB) throws MyDBException {
        final String url_local = "jdbc:mysql://localhost:3306/" + nomDB + "?serverTimezone=UTC";
        final String url_remote = "jdbc:mysql://172.23.85.187:3306/" + nomDB + "?serverTimezone=UTC";
        final String user = "223";
        final String password = "emf123";

        System.out.println("url:" + url_remote);
        try {
            dbConnexion = DriverManager.getConnection(url_remote, user, password);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void connecterBdHSQLDB(String nomDB) throws MyDBException {
        final String url = "jdbc:hsqldb:file:" + nomDB + ";shutdown=true";
        final String user = "SA";
        final String password = "";
        System.out.println("url:" + url);
        try {
            dbConnexion = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void connecterBdAccess(String nomDB) throws MyDBException {
        final String url = "jdbc:ucanaccess://" + nomDB;
        System.out.println("url=" + url);
        try {
            dbConnexion = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void deconnecter() throws MyDBException {
        try {
            if (dbConnexion != null) {
                dbConnexion.close();
            }
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }
    
    @Override
    public List<Personne> lirePersonnes() throws MyDBException {
        ArrayList<Personne> listePersonnes = new ArrayList<Personne>();
        Statement st;
        ResultSet rs;
        try {
            st = dbConnexion.createStatement();
            rs = st.executeQuery("select PK_PERS, Nom, Prenom, Date_naissance, "
                    + "No_rue, Rue, NPA, Salaire, Ville, Actif, date_modif from t_personne");

           while (rs.next()) {

               Personne test = new Personne(rs.getInt("PK_PERS"),
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getDate("Date_naissance"),
                        rs.getInt("No_rue"),
                        rs.getString("Rue"),
                        rs.getInt("NPA"),
                        rs.getString("Ville"),
                        rs.getBoolean("Actif"),
                        rs.getDouble("Salaire"),
                        rs.getDate("date_modif"));
                listePersonnes.add(test);
            }

       } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
        return listePersonnes;
    }
    
   @Override
    public Personne lire(int PK) throws MyDBException {
        Personne p = null;
        String prep = "select * from 223_personne_1table.t_personne where pk_pers=?";
        try (PreparedStatement ps = dbConnexion.prepareStatement(prep)) {
            ps.setInt(1, PK);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                Date dateNaissance = rs.getDate("date_naissance");
                int noRue = rs.getInt("no_rue");
                String rue = rs.getString("rue");
                int npa = rs.getInt("npa");
                String localite = rs.getString("ville");
                boolean actif = rs.getBoolean("actif");
                double salaire = rs.getDouble("salaire");
                Date dateModif = rs.getDate("date_modif");

                p = new Personne(PK, nom, prenom, dateNaissance, noRue, rue, npa, localite, actif, salaire, dateModif);
            }

        } catch (SQLException e) {
            throw new MyDBException(SystemLib.getFullMethodName(), e.getMessage());
        }

        return p;
    }
    
//    private Personne creerPersonne(ResultSet res){
//       Personne newPersonne = new Personne(res);
//        return newPersonne;
//    }
    
    @Override
    public void creer(Personne p) throws MyDBException {
        String prep = "insert into 223_personne_1table.t_personne (prenom, nom, date_naissance, no_rue,rue, npa, ville, actif, salaire, date_modif) values(?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = dbConnexion.prepareStatement(prep, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getPrenom());
            ps.setString(2, p.getNom());
            ps.setDate(3, new java.sql.Date(p.getDateNaissance().getTime()));
            ps.setInt(4, p.getNoRue());
            ps.setString(5, p.getRue());
            ps.setInt(6, p.getNpa());
            ps.setString(7, p.getLocalite());
            ps.setBoolean(8, p.isActif());
            ps.setDouble(9, p.getSalaire());
            ps.setDate(10, new java.sql.Date(p.getDateModif().getTime()));
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            p.setPkPers(rs.getInt(1));

        } catch (SQLException e) {
            throw new MyDBException(SystemLib.getFullMethodName(), e.getMessage());
        }
    }
    
    @Override
    public void modifier(Personne p) throws MyDBException {
        String prep = "update 223_personne_1table.t_personne set prenom=?,nom=?,date_naissance=?,no_rue=?,rue=?,npa=?,ville=?,actif=?,salaire=?,date_modif=? where pk_pers=?";
        try (PreparedStatement ps = dbConnexion.prepareStatement(prep)) {
            ps.setString(1, p.getPrenom());
            ps.setString(2, p.getNom());
            ps.setDate(3, (Date) p.getDateNaissance());
            ps.setInt(4, p.getNoRue());
            ps.setString(5, p.getRue());
            ps.setInt(6, p.getNpa());
            ps.setString(7, p.getLocalite());
            ps.setBoolean(8, p.isActif());
            ps.setDouble(9, p.getSalaire());
            ps.setDate(10, null);
            ps.setInt(11, p.getPkPers());
            ps.execute();

        } catch (SQLException e) {
            throw new MyDBException(SystemLib.getFullMethodName(), e.getMessage());
        }

    }
    
    @Override
    public void effacer(Personne p) throws MyDBException {
        String prep = "delete from t_personne where pk_pers=?";
        try (PreparedStatement ps = dbConnexion.prepareStatement(prep)) {
            ps.setInt(1, p.getPkPers());
            ps.execute();

        } catch (Exception e) {
            throw new MyDBException(SystemLib.getFullMethodName(), e.getMessage());
        }
    }
}
