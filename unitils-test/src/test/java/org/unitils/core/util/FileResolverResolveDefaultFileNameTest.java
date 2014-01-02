/*
 * Copyright 2013,  Unitils.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.unitils.core.util;

import org.junit.Test;

import java.io.File;
import java.net.URI;

import static org.junit.Assert.assertEquals;

/**
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public class FileResolverResolveDefaultFileNameTest {

    private FileResolver fileResolver = new FileResolver();


    @Test
    public void defaultFileName() throws Exception {
        URI result = fileResolver.resolveDefaultFileName("txt", FileResolverResolveDefaultFileNameTest.class);
        assertEquals("FileResolverResolveDefaultFileNameTest.txt", new File(result).getName());
    }
}
