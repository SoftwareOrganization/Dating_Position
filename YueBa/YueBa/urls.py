from django.conf.urls import patterns, include, url
from django.contrib import admin
from yonghu import views
from yonghu import views

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'YueBa.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^denglu/', views.denglu, name="denglu"),
    url(r'^zhuce/', views.zhuce, name="zhuce"),
    url(r'^showUserInfo/', views.showUserInfo, name="showUserInfo"),
    #url(r'^index/', views.index, name="index"),
    url(r'^newJvhui/', views.newJvhui, name="newJvhui"),
    url(r'^showJvhuiInfo/', views.showJvhuiInfo, name="showJvhuiInfo"),
    url(r'^friendsInMap/', views.friendsInMap, name="friendsInMap"),
    url(r'^communication/', views.communication, name="communication"),
    url(r'^showcommunication/', views.showcommunication, name="showcommunication"),
    url(r'^showFromMessageList/', views.showFromMessageList, name="showFromMessageList"),
    url(r'^showToMessageList/', views.showToMessageList, name="showToMessageList"),
    url(r'^showMessagejilu/', views.showMessagejilu, name="showMessagejilu"),
    url(r'^addFriend/', views.addFriend, name="addFriend"),
    url(r'^confirmFriend/', views.confirmFriend, name="confirmFriend"),
    url(r'^showFriendList/', views.showFriendList, name="showFriendList"),
    url(r'^delFriend/', views.delFriend, name="delFriend"),
    url(r'^delDate/', views.delDate, name="delDate"),
    url(r'^showDateList/', views.showDateList, name="showDateList"),
)
