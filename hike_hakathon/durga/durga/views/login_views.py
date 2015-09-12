# -*- utf-8 -*-
#!/usr/bin/env python
__author__ = "nexus-migration-dev@cisco.com"
__copyright__ = "Copyright 2015, Smart Migrate"
__credits__ = ["nexus-migration-dev@cisco.com"]
__license__ = "Cisco Advanced Services Licence."
__version__ = "1.0"
__maintainer__ = "nexus-migration-dev@cisco.com"
__email__ = "nexus-migration-dev@cisco.com"
__status__ = "Development"

from pyramid.security import remember, forget, authenticated_userid
from pyramid.httpexceptions import HTTPFound, HTTPForbidden, HTTPOk, HTTPUnauthorized
from pyramid.threadlocal import get_current_registry
from pyramid.url import route_url
from pyramid.view import view_config, forbidden_view_config
from pyramid.security import NO_PERMISSION_REQUIRED
from pyramid.response import Response
import datetime
import shutil
import time
import pdb
import os
import json

from durga.util.user_session import UserSession
from durga.util.db_query import *

@view_config(route_name='index', renderer='durga:templates/index.pt')
def index_view(request):

	_set = get_current_registry().settings
	label = _set.get("server.label", "Dev")
	print "server up and running"
	return {'status':"success"}


@view_config(route_name='register.view', renderer="json", permission= NO_PERMISSION_REQUIRED)
def register_view(request):
	print "calling register view"
	if request.method == "POST":
		register_details = request.json_body
		username = register_details.get('username')
		password = register_details.get('password')
		user_type = register_details.get('user_type')
		phone_number = register_details.get('phone_number')
		loc_latitude = register_details.get('loc_latitude',0.0)
		loc_longitude = register_details.get('loc_longitude',0.0)
		
		
		user_session = UserSession(username)
		
		#No authentication present for now. Directly give access.
		headers = remember(request, username)
		request_params = request.GET
		if request_params:
			for k, v in request_params.iteritems():
				came_from = came_from + '&%s=%s' % (k, v)
		
		request.session['user_session'] = user_session
		print "firing register query"
		
		response = Response(content_type='text/plain')
		try:
			print username,password
			db_query_status = insertUserDetails(username,password,user_type,phone_number,loc_latitude,loc_longitude)
			if db_query_status:
				print "registration successful"
				return response
			else:
				return HTTPUnauthorized
		except:
			return HTTPUnauthorized

@view_config(route_name='durga_update.view', renderer="json")
def update_view(request):
	if request.method == "POST":
		register_details = request.json_body
		username = register_details.get('username')
		loc_latitude = register_details.get('loc_latitude',0.0)
		loc_longitude = register_details.get('loc_longitude',0.0)
		

		try:
			db_query_status = updateUserLocation(username,loc_latitude,loc_longitude)
			if db_query_status:
				response = Response(content_type='text/plain')
				return response
			else:
				return HTTPUnauthorized
		except:
			return HTTPUnauthorized
		
@view_config(route_name='durga_find.view', renderer="json")
def find_view(request):
	print "request.method", request.method
	# 
	# register_details = request.json_body
	# print register_details
	# username = register_details.get('username')
	# loc_latitude = register_details.get('loc_latitude',0.0)
	# loc_longitude = register_details.get('loc_longitude',0.0)
		
		
	try:
		from geopy.distance import vincenty
		import copy
		print "caliing getuserlocation"
		username = "nikhila.engineer@gmail.com"
		location_details = getUserLocation()
		
		location_details1 = copy.deepcopy(location_details)
		troubled_user = ()
		if location_details:
			print "location details", location_details
			location_list = []
			final_list = {}
			for locs in location_details1:
				try:
					if (str(locs[0]) == str(username)):
						troubled_user = copy.deepcopy(locs)
				except Exception as e:
					print e
			
			for lcs in location_details1:
				print "112",lcs
				if troubled_user[0] != lcs[0]:
					dist = vincenty((troubled_user[1],troubled_user[2]), (lcs[1],lcs[2])).miles
					print "115 dist", dist
					location_list.append([dist,lcs[0],lcs[3]])
					print "117", location_list
			
			ll = sorted(location_list, key= lambda k:k[0])
			
			for each_user in ll:
				final_list[each_user[1]] = each_user[2]
			print "final_list", final_list
			response = Response(body=json.dumps(final_list), content_type='text/plain')
			return response
		else:
			return HTTPUnauthorized
	except Exception as e:
		print e
		return HTTPUnauthorized