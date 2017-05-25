package com.yangmungi.labs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Yangmun
 */
public class Permuter {    
    public Permuter(Collection origin) {
        this.origin = origin;
    }

    //
    //  Optional Factories
    //
    
    private static class DefaultListBuilder implements Builder<List> {
        public List build() {
            return new ArrayList();
        }
    }
    
    private static class DefaultCollectionOfListBuilder implements Builder<Collection<List>> {
        public Collection<List> build() {
            return new ArrayList();
        }
    }
    
    protected static Builder<List> getDefaultListBuilder() {
        return new DefaultListBuilder();
    }
    
    protected static Builder<Collection<List>> getDefaultCollectionOfListBuilder() {
        return new DefaultCollectionOfListBuilder();
    }
    
    private final Collection origin;    
    
    // Resultant object
    private Collection<List> collection;
    
    private Builder<Collection<List>> collectionOfListBuilder;
    
    // Uses addAll
    private Builder<List> writeListBuilder;
    
    // Uses addAll and subList    
    private Builder<List> readListBuilder;
    
    public void setWriteListBuilder(Builder<List> builder) {
        this.writeListBuilder = builder;
    }
    
    public final Builder<List> getWriteListBuilder() {
        if (this.writeListBuilder == null) {
            this.writeListBuilder = getDefaultListBuilder();
        }
        
        return this.writeListBuilder;
    }
    
    public void setReadListBuilder(Builder<List> builder) {
        this.readListBuilder = builder;
    }
    
    public final Builder<List> getReadListBuilder() {
        if (this.readListBuilder == null) {
            this.readListBuilder = getDefaultListBuilder();
        }
        
        return this.readListBuilder;
    }
    
    public void setCollectionOfListBuilder(Builder<Collection<List>> builder) {
        this.collectionOfListBuilder = builder;
    }
    
    public final Builder<Collection<List>> getCollectionOfListBuilder() {
        if (this.collectionOfListBuilder == null) {
            this.collectionOfListBuilder = getDefaultCollectionOfListBuilder();
        }
        
        return this.collectionOfListBuilder;
    }
    
    public void setCollection(Collection<List> collection) {
        this.collection = collection;
    }
    
    public Collection<List> getCollection() {
        return this.collection;
    }
    
    /**
     * Returns all possible permutations of a ListOperator consisting of
     *  the objects from Collection origin
     */
    public void execute() {
        Collection resultant = this.getCollection();
        if (resultant == null) {
            throw new IllegalStateException("Collection not provided");
        }         
        
        // Only needed for the interface?
        List originList = this.buildReadList();
        originList.addAll(this.origin);
        
        int originListSize = originList.size();            
        int originListSizeMinusOne = originListSize - 1;

        List newSubList;

        for (int i = 0; i < originListSize; i++) {            
            newSubList = this.buildWriteList();
            
            if (i > 0) {
                newSubList.addAll(originList.subList(0, i));
            }

            if (i < originListSizeMinusOne) {
                newSubList.addAll(originList.subList(i + 1, originListSize));
            }

            Permuter recursive = this.buildPermuter(newSubList);
            
            recursive.execute();
            
            Collection<List> recursiveResult = recursive.getCollection();
            
            if (recursiveResult.isEmpty()) {
                recursiveResult.add(this.buildWriteList());
            }
            
            for (List subList : recursiveResult) {
                subList.add(0, originList.get(i));
                resultant.add(subList);
            }  
        }   
    }
    
    protected Permuter buildPermuter(Collection collection) {
        Permuter recursive = new Permuter(collection);
        
        recursive.setCollectionOfListBuilder(this.getCollectionOfListBuilder());
        recursive.setCollection(this.buildCollection());
        recursive.setReadListBuilder(this.getReadListBuilder());
        recursive.setWriteListBuilder(this.getWriteListBuilder());
            
        return recursive;
    }
    
    private Collection<List> buildCollection() {
        return this.getCollectionOfListBuilder().build();
    }
        
    private List buildReadList() {
        return this.getReadListBuilder().build();
    }
 
    private List buildWriteList() {
        return this.getWriteListBuilder().build();
    }
}