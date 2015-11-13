package com.goodgame.profiling.dns;

import java.util.Set;

public class Domainname implements Resolveable {
    private final String name;

    private Domainname( String name ) {
        this.name = name;
    }

    public static Domainname fromString( String name ) {
        return new Domainname( name );
    }

    @Override
    public Set<IPV4Address> resolveWith( DNSClient client ) {
        return client.resolve( name );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Domainname other = (Domainname)obj;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Domainname [name=" + name + "]";
    }
}
