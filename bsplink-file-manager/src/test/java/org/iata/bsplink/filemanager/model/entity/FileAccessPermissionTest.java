package org.iata.bsplink.filemanager.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class FileAccessPermissionTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(FileAccessPermission.class).verify();
    }
}
