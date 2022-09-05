/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.workers;

import app.beans.Personne;
import app.workers.DbWorker;
import app.exceptions.MyDBException;
import java.util.List;

/**
 *
 * @author gouglerQ
 */
public class PersonneManager {

    public PersonneManager() {
    }
    private List<Personne> listePersonnes;
    private int index = 0;   
    
    
    public Personne setPersonnes(List<Personne> listPers){
        Personne result;
        listePersonnes = listPers;
        result = listePersonnes.get(index);
        return result;
    } 
    
    public Personne precedentPersonne(){ 
        Personne result;       
        index = index-1;
        if (index <= -1) {
            index = index +1;
            result = listePersonnes.get(index);
        }    
        result = listePersonnes.get(index);      
        return result;
    }
    


    public Personne suivantPersonne() {    
        Personne result;
        index = index + 1;
        if (index >= listePersonnes.size()) {
            index = index-1;
            result = listePersonnes.get(index);
        }
        result = listePersonnes.get(index);
        return result;
    }
    
    public Personne debutPersonne(){
        index = 0;
        return listePersonnes.get(index);
    }
    
    public Personne finPersonne() {
        index = listePersonnes.size() - 1;
        return listePersonnes.get(index);
    }
    
    public Personne courantPersonne(){       
        return listePersonnes.get(index);
    }
}
