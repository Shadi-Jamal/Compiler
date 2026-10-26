package de.uniwue.jpp.compiler.error;

import de.uniwue.jpp.compiler.syntax.TokenLocation;
import de.uniwue.jpp.compiler.util.NameCache;

public class CannotFindNameError implements Error {
    int name; TokenLocation location;
    public CannotFindNameError(int name, TokenLocation location) {
        this.name=name;
        this.location=location;
    }

    public int getName() {
        return name;
    }

    @Override
    public String getMessage(NameCache names) {
        return "Cannot find name '"+names.getName(name)+"' in this scope.";
    }

    @Override
    public TokenLocation getLocation() {
        return location;
    }

}
