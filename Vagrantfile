# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|
  config.vm.box = "ubuntu/trusty64"
  config.vm.box_check_update = false

  config.vm.define "go" do |go|
    go.vm.hostname = "go.sentry.local"
    go.vm.network "forwarded_port", guest: 8153, host: 8153
  
    go.vm.provider "virtualbox" do |vb|
      vb.memory = "2048"
    end
  end

  config.vm.provision "puppet" do |p|
    p.options = "--verbose"
    p.manifests_path = "provisioning/manifests"
    p.module_path = "provisioning/modules"
    p.hiera_config_path = "provisioning/hiera.yaml"
    p.working_directory = "/tmp/vagrant-puppet"
  end
end

