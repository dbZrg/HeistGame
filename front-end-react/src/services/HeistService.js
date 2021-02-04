import http from '../http-common'

class HeistService {

    getAll() {
        return http.get("/heists");
    }
    getHeist(id) {
        return http.get('/heist/'+id);
    } 
    getHeistSkills(id) {
        return http.get('/heist/'+id+'/skills');
    }
    getHeistMembers(id){
        return http.get('/heist/'+id+'/members');
    }
    getHeistStatus(id){
        return http.get('/heist/'+id+'/status');
    }

    getHeistOutcome(id){
        return http.get('/heist/'+id+'/outcome');
    }
      
}

export default new HeistService();