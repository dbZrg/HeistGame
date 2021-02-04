import React from 'react';
import HeistService from '../services/HeistService';

class HeistComp extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            heist: null,
            
        }
    }

    componentDidMount(){
        HeistService.getHeist(this.props.match.params.id)
            .then(response => this.setState({heist: response.data}))
            .catch(function(error) {
            console.log('Fetch error: ' + error.message);    
             
            })
        }

    render (){
        const heist = this.state.heist;
        if (heist === null) {
            return <span>TEXT</span>
        }


        return (

            <div>
                <h3>name: {heist.name}</h3>
                <h3>location: {heist.location}</h3>
                <p>start time: {heist.startTime}</p>
                <p>end time: {heist.endTime}</p>
                <p>status: {heist.status}</p>

                <h3>Required Skills</h3>
                <ul>
                {heist.skills.map(sub =>
                    <li>
                        <p>name: {sub.name}</p>
                        <p>level: {sub.level}</p>
                        <p>members: {sub.members}</p>
                        
                    </li>
                )}

                </ul>

               
            </div>

        )
    }

}

export default HeistComp