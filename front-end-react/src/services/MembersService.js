import http from '../http-common'

class MemberService {

    getAll() {
        return http.get("/members");
    }
    getMember(id) {
        return http.get('/member/'+id);
    } 
    getMemberSkills(id) {
        return http.get('/member/'+id+'/skills');
    }
      
}

export default new MemberService();