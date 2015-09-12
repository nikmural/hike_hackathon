# -*- utf-8 -*-
#!/usr/bin/env python
__author__ = "nexus-migration-dev@cisco.com"
__copyright__ = "Copyright 2011, Smart Migrate"
__credits__ = ["nexus-migration-dev@cisco.com"]
__license__ = "Cisco Advanced Services Licence."
__version__ = "1.0"
__maintainer__ = "nexus-migration-dev@cisco.com"
__email__ = "nexus-migration-dev@cisco.com"
__status__ = "Development"

from pyramid.config import Configurator
from pyramid.security import NO_PERMISSION_REQUIRED
from pyramid.authentication import AuthTktAuthenticationPolicy
from pyramid.authorization import ACLAuthorizationPolicy
from sqlalchemy import engine_from_config
from pyramid_mailer.mailer import Mailer
from pyramid.events import BeforeRender
from .models.meta import DBSession, Base
from .security import (RootFactory,
                       group_finder,
                       RequestWithUserAttribute)

#def add_renderer_globals(event):
#    event['h'] = helpers
   
#from pyramid.session import UnencryptedCookieSessionFactoryConfig

import pyramid_beaker

def main(global_config, **settings):
    """ This function returns a WSGI application.
    """
    #engine = engine_from_config(settings, 'sqlalchemy.')
    #DBSession.configure(bind=engine)
    
    config = Configurator(settings=settings, root_factory=RootFactory)
#    config = Configurator(settings=settings)
    #config.add_subscriber(add_renderer_globals, BeforeRender)
    # HTML Rederer
    config.add_renderer('.html', 'pyramid.mako_templating.renderer_factory')
    # Configure Beaker sessions
    #session_factory = UnencryptedCookieSessionFactoryConfig(settings)
    session_factory = pyramid_beaker.session_factory_from_settings(settings)
    print session_factory
    config.set_session_factory(session_factory)
    # Authendication/Autherization
    authn_policy = AuthTktAuthenticationPolicy(
                       settings.get('mool.auth_secret'),
                       #secure = True,
                       http_only = True,
                       callback=group_finder)
    config.set_authentication_policy(authn_policy)

    authz_policy = ACLAuthorizationPolicy()
    config.set_authorization_policy(authz_policy)
    # Request Object With user object
    config.set_request_factory(RequestWithUserAttribute)
    # Pyramid_mailer Configuration 
    config.registry['mailer'] = Mailer.from_settings(settings)

    config.add_view('pyramid.view.append_slash_notfound_view',
                    context='pyramid.httpexceptions.HTTPNotFound')
    config.add_static_view('static', 'mool:static', cache_max_age=360000)
    config.add_route('index', '')
    config.add_route('register.view', '/register/')
    config.add_route('durga_update.view', '/durga_update/')
    config.add_route('durga_find.view', '/durga_find/')
    config.scan('durga.views')

    #Base.metadata.create_all(engine)
    #initialize_connection()
    return config.make_wsgi_app()
