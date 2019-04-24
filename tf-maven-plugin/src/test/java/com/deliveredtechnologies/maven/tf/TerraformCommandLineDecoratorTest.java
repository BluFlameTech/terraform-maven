package com.deliveredtechnologies.maven.tf;

import com.deliveredtechnologies.maven.io.Executable;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TerraformCommandLineDecoratorTest {

  @Test
  public void executeDelegatesToExecutable() throws IOException, InterruptedException {
    String response = "Success!";
    String terraformCommand = "terraform init ";
    Executable executable = Mockito.mock(Executable.class);
    Mockito.when(executable.execute(terraformCommand)).thenReturn(response);
    TerraformCommandLineDecorator terraform = new TerraformCommandLineDecorator(TerraformCommand.INIT, executable);

    String output = terraform.execute("");
    Assert.assertEquals(response, output);
    Mockito.verify(executable, Mockito.times(1)).execute(terraformCommand);
  }

  @Test
  public void executeDelegatesToExecutableWithTimeout() throws IOException, InterruptedException {
    String response = "Success!";
    int timeout = 1000;
    String terraformCommand = "terraform init ";
    Executable executable = Mockito.mock(Executable.class);
    Mockito.when(executable.execute(terraformCommand, timeout)).thenReturn(response);
    TerraformCommandLineDecorator terraform = new TerraformCommandLineDecorator(TerraformCommand.INIT, executable);

    String output = terraform.execute("", timeout);
    Assert.assertEquals(response, output);
    Mockito.verify(executable, Mockito.times(1)).execute(terraformCommand, timeout);
  }

  @Test(expected = IOException.class)
  public void terraformCommandLineDecoratorErrorsOnCreationWhenTfSourceDirIsNotFound() throws IOException {
    TerraformCommandLineDecorator terraformCommandLineDecorator = new TerraformCommandLineDecorator(TerraformCommand.APPLY);
  }

  @Test
  public void terraformCommandLineDecoratorSucceedsOnCreationWhenTfSourceDirIsFound() throws IOException {
    Path tfSrcPath = Paths.get("src", "main", "tf");
    Path rootModulePath = tfSrcPath.resolve("tf").resolve("root");
    FileUtils.forceMkdir(rootModulePath.toFile());
    TerraformCommandLineDecorator terraformCommandLineDecorator = new TerraformCommandLineDecorator(TerraformCommand.APPLY);
    FileUtils.forceDelete(tfSrcPath.toFile());
  }
}
