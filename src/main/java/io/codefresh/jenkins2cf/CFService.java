/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.codefresh.jenkins2cf;

/**
 *
 * @author antweiss
 */
public class CFService {
    private String name;
    private String id;

    public CFService(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
    
}
