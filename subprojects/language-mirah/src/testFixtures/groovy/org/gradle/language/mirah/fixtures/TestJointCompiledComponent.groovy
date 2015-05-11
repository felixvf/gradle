/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.language.mirah.fixtures

import org.gradle.integtests.fixtures.jvm.JvmSourceFile
import org.gradle.integtests.fixtures.jvm.TestJvmComponent
import org.gradle.language.mirah.MirahLanguageSourceSet

class TestJointCompiledComponent extends TestJvmComponent {

    String languageName = "mirah"
    String sourceSetTypeName = MirahLanguageSourceSet.class.name

    List<JvmSourceFile> sources = [
            new JvmSourceFile("compile/test", "Person.mirah", '''
package compile.test

class Person
  attr_reader name:String
  attr_reader age:int
  
  def initialize(name:String,age:int)
    @name = name
    @age  = age
  end
  
  
  def toString
  	"#{@name}, #{age}"
  end
end
'''),
            new JvmSourceFile("compile/test", "Person2.java", '''
package compile.test

class Person2
  attr_accessor name:String
  attr_accessor age:int
end
''')
    ]

    @Override
    List<String> getSourceFileExtensions() {
        return ["java", "mirah"]
    }

}
