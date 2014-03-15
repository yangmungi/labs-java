package com.yangmungi.labs;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author Yangmun
 */
public class Permuter {
    private final Collection origin;    
    
    private Collection<List> collection;
    
    // This list should have supremely optimal add()
    private ListBuilder writeListBuilder;
    
    // This list should have supremely optimal subList()     
    private ListBuilder readListBuilder;
    
    public Permuter(Collection origin) {
        this.origin = origin;
    }
    
    /**
     * Returns a Collection of Collections
     * @return 
     */
    public Collection<List> permute() {
        if (this.collection == null) {
            throw new java.lang.IllegalStateException("Collection not provided");
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
                    
        }   
        
        return null;
    }
    
    private List buildReadList() {
        return this.readListBuilder.build();
    }
 
    private List buildWriteList() {
        return this.writeListBuilder.build();
    }
}