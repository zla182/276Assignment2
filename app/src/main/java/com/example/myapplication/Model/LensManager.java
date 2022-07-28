package com.example.myapplication.Model;
/**
 * LensManager
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LensManager implements Iterable<Lens>{
    //store arraylist of Lens
    private List<Lens> Lenses= new ArrayList<>();

    private static LensManager instance;

    private LensManager(){

    }

    public static LensManager getInstance(){
        if(instance==null){
            instance=new LensManager();
            instance.add(new Lens("Canon", 1.8, 50));
            instance.add(new Lens("Tamron", 2.8, 90));
            instance.add(new Lens("Sigma", 2.8, 200));
            instance.add(new Lens("Nikon", 4, 200));
        }
        return instance;
    }

    //add method
    public void add(Lens lens){
        Lenses.add(lens);
    }

    public Lens get(int i){
        return Lenses.get(i);
    }

    public void remove(int index){ Lenses.remove(index); }


    @Override
    public Iterator<Lens> iterator() {
        return Lenses.iterator();
    }

    public int size(){
        return Lenses.size();
    }

}
