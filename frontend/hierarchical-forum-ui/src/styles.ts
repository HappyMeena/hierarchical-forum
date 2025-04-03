export const formContainer = {
    height: '100%',
    display: 'flex',
    flexDirection: 'column' as const
};

export const formTitle = {
    marginTop: '0',
    marginBottom: '20px',
    fontSize: '1.5rem',
    color: '#333',
    textAlign: 'center' as const
};

export const inputField = {
    width: '100%',
    padding: '12px',
    border: '1px solid #ddd',
    borderRadius: '6px',
    fontSize: '16px',
    marginBottom: '15px',
    boxSizing: 'border-box' as const
};

export const submitButton = {
    width: '100%',
    padding: '12px',
    backgroundColor: '#4a6bff', 
    color: 'white',
    border: 'none',
    borderRadius: '6px',
    cursor: 'pointer',
    fontSize: '16px',
    fontWeight: 'bold' as const,
    transition: 'background-color 0.3s',
    ':hover': {
        backgroundColor: '#3a5bef'
    },
    ':disabled': {
        backgroundColor: '#cccccc',
        cursor: 'not-allowed'
    }
};

export const errorMessage = {
    color: '#ff4444',
    marginBottom: '15px',
    padding: '10px',
    backgroundColor: '#ffeeee',
    borderRadius: '4px',
    fontSize: '14px'
};