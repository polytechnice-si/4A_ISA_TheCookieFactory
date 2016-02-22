package fr.unice.polytech.isa.tcf.components;

import fr.unice.polytech.isa.tcf.CatalogueExploration;
import fr.unice.polytech.isa.tcf.entities.Cookies;

import javax.ejb.Stateless;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Stateless
public class CatalogueBean implements CatalogueExploration {


	@Override
	public Set<Cookies> listPreMadeRecipes() {
		return new HashSet(Arrays.asList(Cookies.values()));
	}

	@Override
	public Set<Cookies> exploreCatalogue(String regexp) {
		Set<Cookies> result = new HashSet<>();
		for(Cookies c: Cookies.values()) {
			if(c.name().contains(regexp))
				result.add(c);
		}
		return result;
	}
}
