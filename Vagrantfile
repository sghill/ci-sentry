# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure(2) do |config|
  config.vm.box = "ubuntu/trusty64"
  config.vm.network "forwarded_port", guest: 8153, host: 8153

  config.vm.provider "virtualbox" do |vb|
    vb.memory = "2048"
  end

  config.vm.provision "puppet" do |p|
    p.manifests_path = "provisioning/manifests"
    p.module_path = "provisioning/modules"
    p.options = "--verbose"
    p.hiera_config_path = "provisioning/hiera.yaml"
    p.working_directory = "/tmp/vagrant-puppet"
  end
end

