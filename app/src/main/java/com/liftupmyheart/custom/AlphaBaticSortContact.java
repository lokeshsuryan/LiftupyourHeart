package com.liftupmyheart.custom;

import com.liftupmyheart.model.ContactDao;

import java.util.Comparator;

public class AlphaBaticSortContact implements Comparator<ContactDao> {

    @Override
    public int compare(ContactDao o1, ContactDao o2) {
        int nameComp = 0;
        try {
            String name1 = o1.getName().toLowerCase();
            String name2 = o2.getName().toLowerCase();
            nameComp = name1.compareTo(name2);

            return nameComp;
        } catch (Exception e) {
            return nameComp;
        }
    }
}
