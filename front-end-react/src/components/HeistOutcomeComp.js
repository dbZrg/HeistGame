import React from 'react';
import HeistService from '../services/HeistService';

class HeistOutcomeComp extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            status: null,
            
        }
    }

    componentDidMount(){
        HeistService.getHeistOutcome(this.props.match.params.id)
            .then(response => this.setState({status: response.data}))
            .catch(function(error) {
            console.log('Fetch error: ' + error.message);    
             
            })
        }

    render (){
        const status = this.state.status;
        if (status === null) {
            return <span>TEXT</span>
        }


        return (

            <div>
               
                <p>status: {status.outcome}</p>

            </div>

        )
    }

}

export default HeistOutcomeComp