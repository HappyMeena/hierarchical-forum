import React, { useState, useEffect } from 'react';
import Register from './components/register';
import Login from './components/login';
import MessageList from './components/messageList';
import PostMessage from './components/postMessage';

const App: React.FC = () => {
    const [user, setUser] = useState<any>(null);
    const [loading, setLoading] = useState(true);
    const [refreshKey, setRefreshKey] = useState(0);

    useEffect(() => {
        const checkAuth = () => {
            try {
                const storedUser = JSON.parse(
                    localStorage.getItem('user') ||
                    sessionStorage.getItem('user') ||
                    'null'
                );
                setUser(storedUser && storedUser.id ? storedUser : null);
            } catch (error) {
                console.error('解析用户数据出错:', error);
                setUser(null);
            }
            setLoading(false);
        };

        checkAuth();
        window.addEventListener('storage', checkAuth);
        return () => window.removeEventListener('storage', checkAuth);
    }, []);

    const handleLoginSuccess = (userData: any) => {
        setUser(userData);
    };

    const handleLogout = () => {
        localStorage.removeItem('user');
        sessionStorage.removeItem('user');
        setUser(null);
    };

    const handlePostSuccess = () => {
        setRefreshKey(prev => prev + 1);
    };

    if (loading) {
        return <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: '100vh'
        }}>加载中...</div>;
    }

    return (
        <div style={{
            maxWidth: '800px',
            margin: '0 auto',
            padding: '20px',
            fontFamily: 'Arial, sans-serif'
        }}>
            {user?.id ? (
                <>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center',
                        marginBottom: '25px',
                        padding: '15px',
                        backgroundColor: '#f5f5f5',
                        borderRadius: '6px',
                        boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
                    }}>
                        <div>
                            <p style={{
                                margin: '0 0 5px 0',
                                fontWeight: 'bold'
                            }}>欢迎回来！</p>
                            <p style={{
                                margin: 0,
                                color: '#555',
                                fontSize: '0.95em'
                            }}>
                                <strong>{user.username}</strong> ({user.email})
                            </p>
                        </div>
                        <button
                            onClick={handleLogout}
                            style={{
                                padding: '6px 12px',
                                background: '#ff4444',
                                color: 'white',
                                border: 'none',
                                borderRadius: '4px',
                                cursor: 'pointer',
                                fontSize: '0.9em'
                            }}
                        >
                            退出登录
                        </button>
                    </div>

                    <div style={{
                        marginBottom: '30px',
                        padding: '20px',
                        backgroundColor: 'white',
                        borderRadius: '6px',
                        boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
                    }}>
                        <PostMessage onSuccess={handlePostSuccess} />
                    </div>

                    <MessageList refreshKey={refreshKey} />
                </>
            ) : (
                <>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'center',
                        gap: '30px',
                        marginBottom: '30px',
                        flexWrap: 'wrap'
                    }}>
                        <div style={{
                            flex: '1',
                            minWidth: '300px',
                            maxWidth: '400px'
                        }}>
                            <Register />
                        </div>
                        <div style={{
                            flex: '1',
                            minWidth: '300px',
                            maxWidth: '400px'
                        }}>
                            <Login onLoginSuccess={handleLoginSuccess} />
                        </div>
                    </div>

                    {/* 添加这行 - 在未登录状态下也显示留言列表 */}
                    <MessageList refreshKey={refreshKey} />
                </>
            )}
        </div>
    );
};

export default App;