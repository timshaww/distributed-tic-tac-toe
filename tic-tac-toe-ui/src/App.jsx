import { useState, useEffect } from 'react';
import React from 'react';
import { Play, Circle, X, AlertTriangle } from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';

export default function TicTacToe() {
	const [sessionId, setSessionId] = useState(null);
	const [board, setBoard] = useState([
		['-', '-', '-'],
		['-', '-', '-'],
		['-', '-', '-'],
	]);
	const [status, setStatus] = useState('Waiting to start');
	const [statusMessage, setStatusMessage] = useState('');
	const [error, setError] = useState(null);

	const startGame = async () => {
		try {
			const response = await fetch('http://localhost:8081/sessions', {
				method: 'POST',
			});
			if (!response.ok) throw new Error('Failed to start game');
			const newSessionId = await response.text();
			setSessionId(newSessionId);
			simulateGame(newSessionId);
		} catch (err) {
			setError(err.message);
		}
	};

	const simulateGame = async (id) => {
		try {
			const response = await fetch(`http://localhost:8081/sessions/${id}/simulate`, {
				method: 'POST',
			});
			if (!response.ok) throw new Error('Failed to simulate game');
			fetchGameState(id);
		} catch (err) {
			setError(err.message);
		}
	};

	const fetchGameState = async (id) => {
		try {
			const response = await fetch(`http://localhost:8081/sessions/${id}`);
			if (!response.ok) throw new Error('Failed to fetch game state');
			const data = await response.json();
			setStatus(data.status);
			fetchGameBoard(data.gameId);
		} catch (err) {
			setError(err.message);
		}
	};

	const fetchGameBoard = async (gameId) => {
		try {
			const response = await fetch(`http://localhost:8080/games/${gameId}`);
			if (!response.ok) throw new Error('Failed to fetch game board');
			const gameData = await response.json();
			const formattedBoard = gameData.board.map((row) => row.split(''));
			setBoard(formattedBoard);
			setStatus(gameData.status);
		} catch (error) {
			console.error('Error fetching game board:', error);
		}
	};

	useEffect(() => {
		if (sessionId && status === 'IN_PROGRESS') {
			const interval = setInterval(() => fetchGameState(sessionId), 1000);
			return () => clearInterval(interval);
		}
	}, [sessionId]);

	useEffect(() => {
		switch (status) {
			case 'IN_PROGRESS':
				setStatusMessage('In Progress...');
				break;
			case 'X_WINS':
				setStatusMessage('X Wins! 🎉');
				break;
			case 'O_WINS':
				setStatusMessage('O Wins! 🎉');
				break;
			case 'DRAW':
				setStatusMessage("It's a Draw! 🤝");
				break;
			default:
				setStatusMessage('Waiting to start');
		}
	}, [status]);

	return (
		<div className='flex flex-col items-center justify-center h-screen w-full relative'>
			<div className='flex flex-col items-center mt-10'>
				<button
					onClick={startGame}
					className='mb-4 px-6 py-3 bg-blue-500 text-white font-semibold rounded-lg flex items-center gap-2 shadow-lg hover:bg-blue-600 transition-all cursor-pointer'
				>
					<Play size={20} /> Start Simulation
				</button>

				<div className='grid grid-cols-3 gap-2 border border-gray-300 p-4 bg-gray-100 rounded-lg shadow-lg'>
					{board.map((row, i) =>
						row.map((cell, j) => (
							<motion.div
								key={`${sessionId}-${i}-${j}-${cell}`} // Ensures fresh re-render when session changes
								className='w-20 h-20 flex items-center justify-center border border-gray-300 text-3xl font-bold bg-white rounded shadow-sm'
								initial={{ scale: 1, rotate: 0, x: 0 }} // Ensures animations reset
								animate={
									status && typeof status === 'string' // Ensure status is a valid string
										? status.includes('O_WINS') && cell === 'O'
											? {
													scale: [1, 1.2, 1],
													rotate: [0, 10, -10, 0],
													transition: { duration: 0.5, repeat: 2 },
											  }
											: status.includes('X_WINS') && cell === 'X'
											? {
													scale: [1, 1.2, 1],
													rotate: [0, 10, -10, 0],
													transition: { duration: 0.5, repeat: 2 },
											  }
											: status.includes('DRAW')
											? {
													x: [-3, 3, -3, 3, 0], // Shake effect
													transition: { duration: 0.1, repeat: 3 },
											  }
											: { scale: 1, rotate: 0, x: 0 } // Ensures return to normal
										: { scale: 1, rotate: 0, x: 0 }
								}
							>
								{cell === '-' ? '' : cell === 'X' ? <X size={48} /> : <Circle size={48} />}
							</motion.div>
						))
					)}
				</div>
				<p className='mt-4 text-lg font-medium bg-gray-200 px-4 py-2 rounded-lg shadow-md'>Status: {statusMessage}</p>
			</div>
			{error && (
				<div className='absolute bottom-0 right-0 m-6 p-3 bg-red-500 text-white rounded-lg flex items-center gap-2'>
					<AlertTriangle size={20} /> {error}
				</div>
			)}
		</div>
	);
}
