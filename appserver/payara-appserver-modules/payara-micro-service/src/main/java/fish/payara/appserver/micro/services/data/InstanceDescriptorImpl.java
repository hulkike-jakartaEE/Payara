/*

 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright (c) 2016-2018 Payara Foundation. All rights reserved.

 The contents of this file are subject to the terms of the Common Development
 and Distribution License("CDDL") (collectively, the "License").  You
 may not use this file except in compliance with the License.  You can
 obtain a copy of the License at
 https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 or packager/legal/LICENSE.txt.  See the License for the specific
 language governing permissions and limitations under the License.

 When distributing the software, include this License Header Notice in each
 file and include the License file at packager/legal/LICENSE.txt.
 */
package fish.payara.appserver.micro.services.data;

import static java.lang.Boolean.TRUE;
import static javax.json.stream.JsonGenerator.PRETTY_PRINTING;

import java.io.StringWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import org.glassfish.internal.data.ApplicationInfo;

import fish.payara.micro.data.ApplicationDescriptor;
import fish.payara.micro.data.InstanceDescriptor;
import fish.payara.micro.data.ModuleDescriptor;

/**
 *
 * @author steve
 */
public class InstanceDescriptorImpl implements InstanceDescriptor {

    private static final long serialVersionUID = 1L;

    private final String memberUUID;
    private String instanceName;
    private final List<Integer> httpPorts;
    private final List<Integer> httpsPorts;
    private InetAddress hostName;
    private Map<String, ApplicationDescriptor> deployedApplications;
    private boolean liteMember;
    private String instanceType;
    private int hazelcastPort;
    private int adminPort;
    private String instanceGroup;

    public InstanceDescriptorImpl(String UUID) throws UnknownHostException {
        hostName = InetAddress.getLocalHost();
        memberUUID = UUID;
        httpPorts = new ArrayList<>();
        httpsPorts = new ArrayList<>();
    }

    public void addApplication(ApplicationInfo info) {
        if (deployedApplications == null) {
            deployedApplications = new HashMap<>(3);
        }

        ApplicationDescriptorImpl ad = new ApplicationDescriptorImpl(info);
        deployedApplications.put(ad.getName(), ad);
    }

    public void addApplication(ApplicationDescriptor descriptor) {
        if (deployedApplications == null) {
            deployedApplications = new HashMap<>(3);
        }

        deployedApplications.put(descriptor.getName(), descriptor);
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    /**
     * @return the memberUUID
     */
    @Override
    public String getMemberUUID() {
        return memberUUID;
    }

    /**
     * @return the httpPorts
     */
    @Override
    public List<Integer> getHttpPorts() {
        return httpPorts;
    }

    /**
     * @param httpPort the httpPort to add
     */
    public void addHttpPort(int httpPort) {
        httpPorts.add(httpPort);
    }

    /**
     * @return the hostName
     */
    @Override
    public InetAddress getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostName to set
     */
    public void setHostName(InetAddress hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the deployedApplications
     */
    @Override
    public Collection<ApplicationDescriptor> getDeployedApplications() {
        if (deployedApplications == null) {
            return new HashSet<>();
        }
        return deployedApplications.values();
    }

    /**
     * @return the httpsPorts
     */
    @Override
    public List<Integer> getHttpsPorts() {
        return httpsPorts;
    }

    /**
     * @param httpsPort the httpsPort to add
     */
    public void addHttpsPort(int httpsPort) {
        httpsPorts.add(httpsPort);
    }

    public void removeApplication(ApplicationDescriptor applicationInfo) {
        if (deployedApplications == null) {
            deployedApplications = new HashMap<>(3);
        }

        deployedApplications.remove(applicationInfo.getName());
    }

    /**
     * Overrides equals purely based on the UUID value
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (InstanceDescriptorImpl.class.isInstance(obj)) {
            InstanceDescriptorImpl descriptor = (InstanceDescriptorImpl) obj;
            result = this.memberUUID.equals(descriptor.memberUUID);
        }
        return result;
    }

    /**
     * Overrides hashcode based purely on the UUID hashcode
     *
     * @return
     */
    @Override
    public int hashCode() {
        return memberUUID.hashCode();
    }

    /**
     * Checks whether or not this instance is described as a Lite Hazelcast
     * member
     *
     * @return true if this instance describes a Hazelcast Lite member
     */
    @Override
    public boolean isLiteMember() {
        return liteMember;
    }

    /**
     * Sets whether or not this descriptor describes a Hazelcast Lite member
     *
     * @param isLiteMember true if this descriptor describes a Hazelcast Lite
     * member
     */
    public void setLiteMember(boolean isLiteMember) {
        liteMember = isLiteMember;
    }

    /**
     * Checks whether or not this descriptor describes a Payara Micro instance
     *
     * @return true if this descriptor describes a Payara Micro instances
     */
    @Override
    public boolean isMicroInstance() {
        return instanceType.equals("MICRO");
    }

    /**
     * Checks whether or not this descriptor describes a Payara Server instance
     * or the DAS
     *
     * @return true if this descriptor describes a Payara Server instance or the
     * DAS
     */
    @Override
    public boolean isPayaraInstance() {
        return (instanceType.equals("DAS") || instanceType.equals("INSTANCE"));
    }

    /**
     * Sets what instance type this descriptor describes
     *
     * @param instanceType the instance type that this descriptor should
     * describe
     */
    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    /**
     * Gets the instance type that this descriptor describes
     *
     * @return the instance type that this descriptor describes
     */
    @Override
    public String getInstanceType() {
        return instanceType;
    }

    /**
     * Sets the Hazelcast port number for this instance descriptor
     *
     * @param hazelcastPort the port number in use by Hazelcast
     */
    public void setHazelcastPort(int hazelcastPort) {
        this.hazelcastPort = hazelcastPort;
    }

    /**
     * Gets the Hazelcast port number of this instance descriptor
     *
     * @return the port number in use by Hazelcast
     */
    @Override
    public int getHazelcastPort() {
        return hazelcastPort;
    }

    /**
     * Sets the admin port number for this instance descriptor
     *
     * @param adminPort the admin port number in use by this instance
     */
    public void setAdminPort(int adminPort) {
        this.adminPort = adminPort;
    }

    /**
     * Gets the admin port number for this instance descriptor
     *
     * @return the admin port number in use by this instance
     */
    @Override
    public int getAdminPort() {
        return adminPort;
    }

    @Override
    public String toJsonString(boolean verbose) {
        StringWriter writer = new StringWriter();
        Map<String, Object> writerConfig = new HashMap<>();
        writerConfig.put(PRETTY_PRINTING, TRUE);
        try (JsonWriter jsonWriter = Json.createWriterFactory(writerConfig).createWriter(writer)) {
            jsonWriter.writeObject(toJsonObject(verbose));
            return writer.toString();
        }
    }

    private JsonObject toJsonObject(boolean verbose) {
        // Create a builder to store the object
        JsonObjectBuilder configBuilder = Json.createObjectBuilder();

        // Add all the instance information
        configBuilder.add("Host", hostName.getCanonicalHostName());
        configBuilder.add("Http Port(s)", Arrays.toString(getHttpPorts().toArray()).replaceAll("[\\[\\]]", ""));
        configBuilder.add("Https Port(s)", Arrays.toString(getHttpsPorts().toArray()).replaceAll("[\\[\\]]", ""));
        configBuilder.add("Instance Name", this.instanceName);
        configBuilder.add("Instance Group", this.instanceGroup);
        if (memberUUID != null) {
            configBuilder.add("Hazelcast Member UUID", this.memberUUID);
        }

        // Create array of applications
        JsonArrayBuilder deploymentBuilder = Json.createArrayBuilder();
        getDeployedApplications().forEach(app -> {
            // Construct object for each application
            JsonObjectBuilder appBuilder = Json.createObjectBuilder();

            // Add the name of the application
            appBuilder.add("Name", app.getName());

            // If there's only one module in the application, print the module info with the application name
            if (app.getModuleDescriptors().size() == 1) {
                ModuleDescriptor module = app.getModuleDescriptors().get(0);
                appBuilder.add("Type", module.getType());
                appBuilder.add("Context Root", (module.getContextRoot() == null)? "N/A": module.getContextRoot());

                // List the module mappings if verbose is specified
                if (verbose) {
                    JsonObjectBuilder servletMappings = Json.createObjectBuilder();
                    module.getServletMappings().forEach((key, value) -> {
                        servletMappings.add(key, value);
                    });
                    appBuilder.add("Mappings", servletMappings.build());
                }
            
            // If there's more modules, print info for each module
            } else {
                JsonArrayBuilder modules = Json.createArrayBuilder();
                // Construct object for each module
                app.getModuleDescriptors().forEach(module -> {
                    JsonObjectBuilder moduleBuilder = Json.createObjectBuilder();

                    // Add basic information for the module
                    moduleBuilder.add("Name", module.getName());
                    moduleBuilder.add("Type", module.getType());
                    moduleBuilder.add("Context Root",
                            (module.getContextRoot() == null) ? "***" : module.getContextRoot());

                    // Create an object of mappings if verbose is specified
                    if (verbose) {
                        JsonObjectBuilder servletMappings = Json.createObjectBuilder();
                        module.getServletMappings().forEach((key, value) -> {
                            servletMappings.add(key, value);
                        });
                        moduleBuilder.add("Mappings", servletMappings.build());
                    }

                    modules.add(moduleBuilder.build());
                });
                appBuilder.add("Modules", modules.build());
            }
            deploymentBuilder.add(appBuilder.build());
        });
        configBuilder.add("Deployed", deploymentBuilder.build());

        return Json.createObjectBuilder()
            .add("Instance Configuration", configBuilder.build())
            .build();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nInstance Configuration\n");
        sb.append("Host: ").append(hostName.getCanonicalHostName()).append('\n');
        sb.append("HTTP Port(s): ");
        for (Integer port : getHttpPorts()) {
            sb.append(port).append(' ');
        }
        sb.append('\n');
        sb.append("HTTPS Port(s): ");
        for (Integer port : getHttpsPorts()) {
            sb.append(port).append(' ');
        }
        sb.append('\n');
        sb.append("Instance Name: ").append(instanceName).append('\n');
        sb.append("Instance Group: ").append(instanceGroup).append('\n');
        if (memberUUID != null) {
            sb.append("Hazelcast Member UUID ").append(this.memberUUID).append('\n');
        }
        
        for (ApplicationDescriptor applicationDescriptor : getDeployedApplications()) {
            sb.append("Deployed: ");
            sb.append(applicationDescriptor.getName()).append(" ( ");
            for (ModuleDescriptor moduleDescriptor : applicationDescriptor.getModuleDescriptors()) {
                sb.append(moduleDescriptor.getName()).append(' ').append(moduleDescriptor.getType()).append(' ');
                if (moduleDescriptor.getContextRoot() != null) {
                    sb.append(moduleDescriptor.getContextRoot());
                } else {
                    sb.append("***");
                }
                
                sb.append(" [ ");
                for (Entry<String, String> servletMapping : moduleDescriptor.getServletMappings().entrySet()) {
                    sb.append("< ")
                      .append(servletMapping.getValue()).append(' ').append(servletMapping.getKey())
                      .append(" >");
                }
                sb.append(" ] ");
            }
            sb.append(")\n");
            
            String libraries = applicationDescriptor.getLibraries();
            if (libraries != null) {
                sb.append(' ').append(applicationDescriptor.getLibraries());
            }
        }
        sb.append('\n');
        return sb.toString();
    }

    @Override
    public List<URL> getApplicationURLS() {
        LinkedList<URL> result = new LinkedList<>();
        if (deployedApplications != null) {
            for (Map.Entry<String, ApplicationDescriptor> ai : deployedApplications.entrySet()) {
                for (ModuleDescriptor moduleDescriptor : ai.getValue().getModuleDescriptors()) {
                    String contextRoot = moduleDescriptor.getContextRoot();
                    if (contextRoot != null) {
                        for (Integer httpPort : httpPorts) {
                            try {
                                result.add(new URL("http", hostName.getCanonicalHostName(), httpPort, contextRoot));
                            } catch (MalformedURLException ex) {
                                // ignore
                            }
                        }
                        for (Integer httpsPort : httpsPorts) {
                            try {
                                result.add(new URL("https", hostName.getCanonicalHostName(), httpsPort, contextRoot));
                            } catch (MalformedURLException ex) {
                                // ignore
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Gets the instance group name
     * @return The instance group name
     */
    public String getInstanceGroup() {
        return instanceGroup;
    }
    
    /**
     * Sets the instance group name
     * @param instanceGroup The instance group name
     */
    public void setInstanceGroup(String instanceGroup) {
        this.instanceGroup = instanceGroup;
    }

}
