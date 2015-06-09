#coding:utf-8
import simplejson
from django.http import HttpResponse
import datetime
from models import *
from itertools import chain
# Create your views here.
def zhuce(request):
    if request.method == 'GET':
        Username = request.GET.get('username')
        Password = request.GET.get('password')
        position = request.GET.get('position')
        sex = request.GET.get('sex')
        online = request.GET.get('online')
        user = User.objects.filter(Username = Username)
        if user:
            json={'success':False} 
        else:
            user = User(Username = Username, Password = Password,position = position,sex = sex,online = online)
            user.save()
            json={'success':True}  
    return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def denglu(request):
    if request.method == 'GET':
        Username = request.GET.get('username')
        Password = request.GET.get('password')
        users = User.objects.filter(Username = Username)
        if users:
            user = users[0]
            if user.Password == Password:
                json={'success':True,'userID':user.ID,'sex':user.sex,'newfriend':user.newfriend}
            else:
                json={'success':False}
    return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def newJvhui(request):
    if request.method == 'GET':
        DateName = request.GET.get('datename')
        DateTime = request.GET.get('datetime')
        #DateTime = datetime.datetime.now()
        UserID = request.GET.get('userID')
        Text = request.GET.get('text')
        Destination = request.GET.get('destination')
        Member1ID =request.GET.get('member1ID')
        Member2ID =request.GET.get('member2ID')
        Member3ID =request.GET.get('member3ID')
        Member4ID =request.GET.get('member4ID')
        Member5ID =request.GET.get('member5ID')
        try:
            newJvhuiCrea = Date(UserID=UserID,Member5ID = Member5ID,Member4ID = Member4ID,Member3ID =Member3ID,Member2ID = Member2ID,Member1ID = Member1ID,DateName = DateName, DateTime = DateTime,Text = Text, Destination = Destination)
            newJvhuiCrea.save()
            json={'success':True}
        except Exception, e:
            json={'success':False}
            HttpResponse(simplejson.dumps(json,ensure_ascii = False))

    return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def showDateList(request):
    if request.method == 'GET':
        UserID = request.GET.get('userID')
        jsonSet = {}
        array=[]
        json ={}
        queryset = Date.objects.filter(UserID = UserID)
        mem1 = Date.objects.filter(Member1ID = UserID)
        mem2 = Date.objects.filter(Member2ID = UserID)
        mem3 = Date.objects.filter(Member3ID = UserID)

        mem4 = Date.objects.filter(Member4ID = UserID)
        mem5 = Date.objects.filter(Member5ID = UserID)
        try:
            queryset = queryset|mem1|mem2|mem3|mem4|mem5
            for jvhui in queryset:
                DateName = jvhui.DateName
                DateID = jvhui.DateID
                time = jvhui.DateTime
                jsonSet={'time':time,'DateName':DateName,'DateID': DateID}
                array.append(jsonSet)
        except Exception,e:
            json={'success':False}
            return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
        json = {'success':True,'data':array}
        return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def showJvhuiInfo(request):
    if request.method == 'GET':
        #DateName = request.GET.get('datename')
        DateID = request.GET.get('dateID')

        jvhuis = Date.objects.filter(DateID = DateID)
        if jvhuis:
            jvhui = jvhuis[0]
            #DateID = jvhui.DateID
            UserID = jvhui.UserID
            DateTime = jvhui.DateTime
            Text = jvhui.Text
            Destination = jvhui.Destination

            json={'success':True,'UserID':UserID,'datetime':DateTime,'text':Text,'destination':Destination}
            return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
        else:
             json={'success':False}
             return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def friendsInMap(request):
    if request.method == 'GET':
        DateID = request.GET.get('dateID')
        dates = Date.objects.filter(DateID = DateID)
        if dates:
            date = dates[0]
            members = []
            members.append(date.Member1ID)
            members.append(date.Member2ID)
            members.append(date.Member3ID)
            members.append(date.Member4ID)
            members.append(date.Member5ID)
            positions = {}
            for i in range(5):
                if not(members[i] == 0):
                    user = User.objects.get(ID = members[i])
                    positions[members[i]] = user.position
            json={'success':True,'positions':positions}
            return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
        else:
            json={'success':False}
            return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def communication(request):
    if request.method == 'GET':
        FromID = request.GET.get('fromID')
        ToID = request.GET.get('toID')
        Content = request.GET.get('content')
        isRead = 0
        msgTime = request.GET.get('msgTime')
        try:
            newCommunication = Message(FromID = FromID,ToID = ToID, Content = Content,isRead = isRead,msgTime = msgTime)
            newCommunication.save()
            json={'success':True}
        except Exception, e:
            json={'success':False}
            HttpResponse(simplejson.dumps(json,ensure_ascii = False))

        return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def showFromMessageList(request):
    if request.method == 'GET':
        UserID = request.GET.get('userID')
        messages = Message.objects.filter(FromID=UserID)
        toID={}
        if messages:
            for message in messages:
                toID[message.ToID] = message.MessID
            json={'success':True,'message':toID}
        else:
            json={'success':False}
        return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def showToMessageList(request):
    if request.method == 'GET':
        UserID = request.GET.get('userID')
        messages = Message.objects.filter(ToID=UserID)
        fromID={}
        if messages:
            for message in messages:
                fromID[message.FromID] = message.MessID
            json={'success':True,'message':fromID}
        else:
            json={'success':False}
        return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def showMessagejilu(request):
    if request.method == 'GET':
        FromID = request.GET.get('fromID')
        ToID = request.GET.get('toID')
        messages = Message.objects.filter(FromID = FromID,ToID=ToID)
        messlist = {}
        for message in messages:
            messlist[message.MessID] = message.Content
        json={'success':True,'messlist':messlist}
    else:
         json={'success':False}
    return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def showcommunication(request):
    if request.method == 'GET':
        MessID = request.GET.get('messID')
        messages = Message.objects.filter(MessID = MessID)
        if messages:
            message = messages[0]
            json={'success':True,'FromID':message.FromID,'ToID':message.ToID,'Content':message.Content,'isRead':message.isRead,'msgTime':message.msgTime}
        else:
            json={'success':False}
        return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def showUserInfo(request):
    if request.method == 'GET':
        UserID = request.GET.get('userID')
        users = User.objects.filter(ID = UserID)
        if users:
            json={'success':True,'Username':users[0].Username,'position':users[0].position,'sex':users[0].sex,'online':users[0].online}
        else:
            json={'success':False}
        return HttpResponse(simplejson.dumps(json,ensure_ascii = False))

def addFriend(request):
    if request.method == 'GET':
        UserID = request.GET.get('userID')
        FriendsID = request.GET.get('friendsID')
        users = User.objects.filter(ID = FriendsID)
        if users:
            user = users[0]
            user.newfriend = UserID
            user.save()
            json={'success':True}
        else:
            json={'success':False}
        return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def confirmFriend(request):
    if request.method == 'GET':
        try:
            UserID = request.GET.get('userID')
            FriendsID = request.GET.get('friendsID')
            record1 = Friends(UserID = UserID,FriendID = FriendsID)
            record1.save()
            record2 = Friends(UserID = FriendsID,FriendID = UserID)
            record2.save()
            json={'success':True}

        except Exception,e:
            json={'success':False}
            return HttpResponse(simplejson.dumps(json,ensure_ascii = False))

        return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def showFriendList(request):
    if request.method == 'GET':
        UserID = request.GET.get('userID')
        friends = Friends.objects.filter(UserID = UserID)
        friendList = []
        for friend in friends:
            friendList.append(friend.FriendID)
        json={'success':True,'friends':friendList}
        return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
def delDate(request):
    if request.method == 'GET':
        try:
            DateID = request.GET.get('dateID')
            Date.objects.get(DateID = DateID).delete()
        except Exception,e:
            json={'success':False}
            return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
        json={'success':True}
        return HttpResponse(simplejson.dumps(json,ensure_ascii = False))

def delFriend(request):
    if request.method == 'GET':
        try:
            UserID = request.GET.get('userID')
            FriendID = request.GET.get('friendID')
            Friends.objects.get(UserID = UserID,FriendID=FriendID).delete()
            Friends.objects.get(UserID = FriendID,FriendID=UserID).delete()
            json={'success':True}
        except Exception,e:
            json={'success':False}
            return HttpResponse(simplejson.dumps(json,ensure_ascii = False))

        return HttpResponse(simplejson.dumps(json,ensure_ascii = False))
